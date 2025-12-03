package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.config.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
	private UUID userId;
	private String email;
	private String name;
	private String roleCode;

	// 静的ファクトリメソッド
	public static AuthResponseDTO from(CustomUserDetails user) {
		return new AuthResponseDTO(
				user.getUserId(),
				user.getUsername(),
				user.getName(),
				user.getRoleCode());
	}
}
