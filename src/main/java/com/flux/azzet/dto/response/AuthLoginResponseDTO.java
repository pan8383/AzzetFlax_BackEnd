package com.flux.azzet.dto.response;

import java.util.UUID;

import com.flux.azzet.model.Role;
import com.flux.azzet.security.CustomUserDetails;

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
