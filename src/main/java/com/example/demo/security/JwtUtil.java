package com.example.demo.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.entity.UserEntity;
import com.example.demo.model.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String SECRET_KEY = "aB3dE5gH7jK9mN1pQ2rS4tV6wX8yZ0!@#";
	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1時間

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	/*
	 * トークンを生成する
	 */
	public String generateToken(UserEntity userEntity) {
		return Jwts.builder()
				.setSubject(userEntity.getEmail())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/*
	 * トークンの署名と有効期限を検証する
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
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
				.setSigningKey(getSigningKey())
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
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("role");
	}

	public String generateToken(
			String username,
			UUID userId,
			Role role,
			Collection<? extends GrantedAuthority> authorities) {

		return Jwts.builder()
				.setSubject(username)
				.claim("userId", userId.toString())
				.claim("roleCode", role.toString())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
}