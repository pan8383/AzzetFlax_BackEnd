package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString(exclude = "password")
public class UserRequestDTO {
	
	@NotBlank
	@Size(max = 100)
	private final String name;
	
	@NotBlank
	@Email
	@Size(max = 320)
	private final String email;

	@NotBlank
	@Size(min = 8, max = 100)
	private final String password;
	
	@NotBlank
	@Size(max = 100)
	@JsonProperty("role_cd")
	private final String roleCode;
}