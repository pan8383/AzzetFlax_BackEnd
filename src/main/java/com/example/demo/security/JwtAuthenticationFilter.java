package com.example.demo.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

/**
 * 毎リクエストごとに Cookie のアクセストークンを検証して、
 * ログイン済みユーザーとして認証情報をセットする
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {

		String accessToken = null;

		// Cookie から access_token を取得
		if (request.getCookies() != null) {
			for (var cookie : request.getCookies()) {
				if ("access_token".equals(cookie.getName())) {
					accessToken = cookie.getValue();
					break;
				}
			}
		}

		// トークンがあり、未認証の場合のみ処理
		if (accessToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			if (jwtUtil.validateAccessToken(accessToken)) {
				String email = jwtUtil.extractEmail(accessToken);

				CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();

		return path.startsWith("/api/auth/login")
				|| path.startsWith("/api/auth/logout")
				|| path.startsWith("/api/auth/refresh")
				|| path.startsWith("/api/auth/signup");
	}
}
