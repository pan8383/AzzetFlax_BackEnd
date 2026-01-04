package com.example.demo.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		UserEntity userEntity = userRepository.findByEmailAndIsDeletedFalse(email)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));

		return new CustomUserDetails(
				userEntity.getUserId(),
				userEntity.getEmail(),
				userEntity.getName(),
				userEntity.getPassword(),
				userEntity.getRole(),
				Collections.emptyList(),
				true);
	}
}
