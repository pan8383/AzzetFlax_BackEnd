package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.model.Role;
import com.example.demo.security.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthLoginResponseDTO {

	private UUID userId;
	private String email;
	private String name;
	private Role role;

	public static AuthLoginResponseDTO from(CustomUserDetails user) {
		return new AuthLoginResponseDTO(
				user.getUserId(),
				user.getUsername(),
				user.getName(),
				user.getRole());
	}
}
