package com.example.demo.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
	//@formatter:off
	R001("ADMIN"),
	R002("STAFF"),
	R003("MEMBER"),
	R004("VIEWER"),
	R005("GUEST");
	//@formatter:on

	private final String roleName;
}
