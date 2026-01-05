package com.flux.azzet.config;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.flux.azzet.security.JwtAuthenticationFilter;
import com.flux.azzet.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	// JWT 認証フィルター
	private final JwtAuthenticationFilter jwtFilter;

	// パスワードエンコーダー
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 認証プロバイダ
	@Bean
	DaoAuthenticationProvider authenticationProvider(
			CustomUserDetailsService userDetailsService,
			BCryptPasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	// 認証処理
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	// セキュリティ設定全体
	@Bean
	SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			DaoAuthenticationProvider authenticationProvider) throws Exception {

		http
				.csrf(csrf -> csrf.disable())
				.cors(cors -> {
				})
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/api/auth/signup",
								"/api/auth/login",
								"/api/auth/logout",
								"/api/auth/refresh")
						.permitAll()
						.anyRequest().authenticated())
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.exceptionHandling(ex -> ex
						// 未認証は 401 を返す（ログインしていない・JWTなし・期限切れ）
						.authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType("application/json;charset=UTF-8");
							response.getWriter().write("""
									  {
									    "success": false,
									    "data": null,
									    "error": {
									      "code": "UNAUTHORIZED",
									      "message": "認証が必要です",
									      "status": 401
									    }
									  }
									""");
						})

						// 認可エラーは 403 を返す（ログイン済だが権限不足）
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.setContentType("application/json;charset=UTF-8");
							response.getWriter().write("""
									  {
									    "success": false,
									    "data": null,
									    "error": {
									      "code": "FORBIDDEN",
									      "message": "権限がありません",
									      "status": 403
									    }
									  }
									""");
						}));

		// JWT フィルターを追加
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// CORS設定
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
	    config.addAllowedOrigin("https://azzet-flux.kyo-miyaji.site");
	    config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
