package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.example.demo.config.JwtProperties;
import com.example.demo.model.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private Key signingKey;

	@PostConstruct
	private void init() {
		this.signingKey = Keys.hmacShaKeyFor(
				jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
	}

	/*
	 * アクセストークンを生成する
	 */
	public String generateAccessToken(String username, UUID userId, Role role) {
		return Jwts.builder()
				.setSubject(username)
				.claim("userId", userId.toString())
				.claim("role", role.toString())
				.claim("tokenType", "ACCESS")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	/*
	 * リフレッシュトークンを生成する
	 */
	public String generateRefreshToken(UUID userId) {
		return Jwts.builder()
				.setSubject(userId.toString())
				.claim("tokenType", "REFRESH")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	/*
	 * アクセストークンの署名と有効期限を検証する
	 */
	public boolean validateAccessToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(signingKey)
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * トークンを解析し、メールアドレスを抽出する
	 */
	public String extractEmail(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	/*
	 * トークンを解析し、権限を抽出する
	 */
	public String extractRole(String token) {
		return (String) Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("role");
	}

	/**
	 * リフレッシュトークンの検証
	 * @param refreshToken
	 * @return
	 */
	public boolean validateRefreshToken(String refreshToken) {
		try {
			var claims = Jwts.parserBuilder()
					.setSigningKey(signingKey)
					.build()
					.parseClaimsJws(refreshToken)
					.getBody();

			// tokenType が REFRESH か確認
			String tokenType = claims.get("tokenType", String.class);
			return "REFRESH".equals(tokenType);

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * リフレッシュトークンからユーザーIDを取得
	 * @param refreshToken
	 * @return
	 */
	public UUID extractUserIdFromRefreshToken(String refreshToken) {
		String userId = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(refreshToken)
				.getBody()
				.getSubject();

		return UUID.fromString(userId);
	}
}