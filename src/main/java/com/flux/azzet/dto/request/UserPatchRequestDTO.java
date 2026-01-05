package com.flux.azzet.dto.request;

import com.flux.azzet.model.Role;

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
