package com.example.demo.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

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

		String token = null;

		// Cookie から token を取得
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("token".equals(cookie.getName())) {
					token = cookie.getValue();
				}
			}
		}

		if (token != null && jwtUtil.validateToken(token)) {
			String username = jwtUtil.extractEmail(token);

			try {
				CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} catch (UsernameNotFoundException e) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write("""
						{"status":401,"message":"リクエストは認証されていません"}
						""");
				return;
			}

		}

		filterChain.doFilter(request, response);
	}

	/**
	 * JWT 認証を除外するURL
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getServletPath();

		// JWT 認証をスキップするパス
		return path.startsWith("/api/user/signup")
				|| path.startsWith("/api/login")
				|| path.startsWith("/api/logout");
	}
}
