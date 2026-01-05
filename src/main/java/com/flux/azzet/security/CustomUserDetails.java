package com.flux.azzet.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.flux.azzet.model.Role;

public class CustomUserDetails implements UserDetails {
	private final UUID userId;
	private final String username;
	private final String name;
	private final String password;
	private final Role role;
	private final Collection<? extends GrantedAuthority> authorities;
	private final boolean enabled;

	public CustomUserDetails(
			UUID userId,
			String username,
			String name,
			String password,
			Role role,
			Collection<? extends GrantedAuthority> authorities,
			boolean enabled) {
		this.userId = userId;
		this.username = username;
		this.name = name;
		this.password = password;
		this.role = role;
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

	public Role getRole() {
		return role;
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
