package com.flux.azzet.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.flux.azzet.entity.UserEntity;
import com.flux.azzet.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	private UUID id;
	private String name;
	private String email;
	private Role role;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static UserResponseDTO from(UserEntity u) {
		return UserResponseDTO.builder()
				.id(u.getUserId())
				.name(u.getName())
				.email(u.getEmail())
				.role(u.getRole())
				.createdAt(u.getCreatedAt())
				.updatedAt(u.getCreatedAt())
				.build();
	}
}
