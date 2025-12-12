package com.example.demo.service;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AuthRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final JwtUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	/**
	 * ユーザー情報をレスポンスボディにセットし、
	 * トークンをCookieにセットする
	 * @param request
	 * @param response
	 * @return
	 */
	public AuthResponseDTO getUserInfoWithSetToken(AuthRequestDTO request, HttpServletResponse response) {

		// メールとパスワードで認証試行する
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()));

		// 認証成功 → SecurityContext に保存
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// CustomUserDetails を取得
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		// JWTトークン生成（CustomUserDetailsの情報で）
		String token = jwtUtil.generateToken(
				userDetails.getUsername(),
				userDetails.getUserId(),
				userDetails.getRole(),
				userDetails.getAuthorities());

		// Cookie にセット
		ResponseCookie cookie = ResponseCookie.from("token", token)
				.httpOnly(true)
				.secure(false) // 本番は true（HTTPS）
				.sameSite("Lax") // 本番は Strict or Lax
				.path("/")
				.maxAge(60 * 60)
				.build();

		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		// レスポンス返却
		return AuthResponseDTO.from(userDetails);
	}

	/**
	 * Cookieのトークンを削除します
	 * @param response
	 */
	public void deleteCookieToken(HttpServletResponse response) {
		// JWTやセッションCookieを削除する
		ResponseCookie cookie = ResponseCookie.from("token", "")
				.httpOnly(true)
				.secure(false)
				.sameSite("Lax")
				.path("/")
				.maxAge(0)
				.build();
		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	//	public AuthResponseDTO getValidateUser(CustomUserDetails userDetails) {
	//		return AuthResponseDTO.from(userDetails);
	//	}
}