package com.flux.azzet.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.stereotype.Component;

import com.flux.azzet.config.JwtProperties;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(
				jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
	}
}
