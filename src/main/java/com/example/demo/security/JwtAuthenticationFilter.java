package com.example.demo.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
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
			CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		filterChain.doFilter(request, response);
	}
}
