package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.model.Role;
import com.example.demo.security.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

	private String accessToken;

	private UUID userId;
	private String email;
	private String name;
	private Role role;

	// ログイン / refresh 用
	public static AuthResponseDTO of(String accessToken, CustomUserDetails user) {
		return new AuthResponseDTO(
				accessToken,
				user.getUserId(),
				user.getUsername(),
				user.getName(),
				user.getRole());
	}

	// /me 用（トークン不要）
	public static AuthResponseDTO from(CustomUserDetails user) {
		return new AuthResponseDTO(
				null,
				user.getUserId(),
				user.getUsername(),
				user.getName(),
				user.getRole());
	}
}
