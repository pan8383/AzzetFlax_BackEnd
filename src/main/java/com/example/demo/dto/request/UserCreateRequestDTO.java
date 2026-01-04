package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString(exclude = "password")
public class UserCreateRequestDTO {

	@NotBlank
	@Size(min = 5, max = 50)
	private final String name;

	@NotBlank
	@Email
	@Size(max = 254)
	private final String email;

	@NotBlank
	@Size(min = 8, max = 50)
	private final String password;

	@NotBlank
	@Size(max = 10)
	private final String role;
}