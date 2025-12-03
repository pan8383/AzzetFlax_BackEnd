package com.example.demo.config;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	private final UUID userId;
	private final String username;
	private final String name;
	private final String password;
	private final String roleCode;
	private final Collection<? extends GrantedAuthority> authorities;
	private final boolean enabled;

	public CustomUserDetails(
			UUID userId,
			String username,
			String name,
			String password,
			String roleCode,
			Collection<? extends GrantedAuthority> authorities,
			boolean enabled) {
		this.userId = userId;
		this.username = username;
		this.name = name;
		this.password = password;
		this.roleCode = roleCode;
		this.authorities = authorities;
		this.enabled = enabled;
	}

	public UUID getUserId() {
		return userId;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	public String getRoleCode() {
		return roleCode;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
