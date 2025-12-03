package com.example.demo.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmailAndIsDeletedFalse(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

		return new CustomUserDetails(
				user.getUserId(),
				user.getEmail(),
				user.getName(),
				user.getPassword(),
				user.getRoleCode(),
				Collections.emptyList(),
				true);
	}

}
