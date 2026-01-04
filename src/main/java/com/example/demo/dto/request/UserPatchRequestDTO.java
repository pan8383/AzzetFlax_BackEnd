package com.example.demo.dto.request;

import com.example.demo.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchRequestDTO {
	private String name;
	private String email;
	private String password;
	private Role role;
}
