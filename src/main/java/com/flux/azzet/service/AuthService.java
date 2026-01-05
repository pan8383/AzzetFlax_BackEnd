package com.flux.azzet.service;

import java.util.UUID;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.flux.azzet.config.EnvConfig;
import com.flux.azzet.dto.request.AuthRequestDTO;
import com.flux.azzet.dto.response.AuthLoginResponseDTO;
import com.flux.azzet.dto.response.AuthResponseDTO;
import com.flux.azzet.entity.UserEntity;
import com.flux.azzet.repository.UserRepository;
import com.flux.azzet.security.CustomUserDetails;
import com.flux.azzet.security.JwtUtil;

@Service
public class AuthService {

	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final EnvConfig envConfig;

	private boolean HTTP_ONRY = true;
	private boolean SECURE;
	private String SAMESITE;

	public AuthService(
			JwtUtil jwtUtil,
			AuthenticationManager authenticationManager,
			UserRepository userRepository,
			EnvConfig envConfig) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.envConfig = envConfig;

		// 環境によって切り替え
		if (envConfig.isProd()) {
			this.SECURE = true;
			this.SAMESITE = "None";
		} else {
			this.SECURE = false;
			this.SAMESITE = "Lax";
		}
	}

	/**
	 * ユーザー情報をレスポンスボディにセットし、
	 * トークンをCookieにセットする
	 * @param request
	 * @param response
	 * @return
	 */
	public AuthLoginResponseDTO login(AuthRequestDTO request, HttpServletResponse response) {

		// 認証
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		// アクセストークンを生成
		String accessToken = jwtUtil.generateAccessToken(
				userDetails.getUsername(),
				userDetails.getUserId(),
				userDetails.getRole());

		// リフレッシュトークン
		String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUserId());

		// access_token Cookie（短命）
		ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
				.httpOnly(HTTP_ONRY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.path("/")
				.maxAge(60 * 60) // 60分
				.build();

		// refresh_token Cookie（長命）
		ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
				.httpOnly(HTTP_ONRY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.path("/api/auth")
				.maxAge(7 * 24 * 60 * 60) // 7日
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

		// レスポンスはユーザー情報だけ
		return AuthLoginResponseDTO.from(userDetails);
	}

	/**
	 * Cookieのトークンを削除します
	 * @param response
	 */
	public void logout(HttpServletResponse response) {
		// access_token 削除
		ResponseCookie deleteAccessCookie = ResponseCookie.from("access_token", "")
				.httpOnly(HTTP_ONRY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.path("/")
				.maxAge(0)
				.build();

		// refresh_token 削除
		ResponseCookie deleteRefreshCookie = ResponseCookie.from("refresh_token", "")
				.httpOnly(HTTP_ONRY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.path("/api/auth")
				.maxAge(0)
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
		response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());
	}

	public AuthResponseDTO getCurrentUser(CustomUserDetails userDetails) {
		return AuthResponseDTO.from(userDetails);
	}

	/**
	 * アクセストークンを再発行
	 * @param refreshToken
	 * @return
	 */
	public void refreshAccessToken(HttpServletResponse response, String refreshToken) {

		// リフレッシュトークン検証
		if (!jwtUtil.validateRefreshToken(refreshToken)) {
			throw new RuntimeException("Invalid refresh token");
		}

		// ユーザーID取得
		UUID userId = jwtUtil.extractUserIdFromRefreshToken(refreshToken);

		// ユーザー取得
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// 新しいアクセストークン生成
		String accessToken = jwtUtil.generateAccessToken(
				user.getEmail(),
				user.getUserId(),
				user.getRole());

		// Cookie にセット
		ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
				.httpOnly(HTTP_ONRY)
				.secure(SECURE)
				.sameSite(SAMESITE)
				.path("/")
				.maxAge(60 * 60) // 60分
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
	}
}