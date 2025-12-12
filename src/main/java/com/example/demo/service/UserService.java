package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserCreateRequestDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.exception.UserException;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * ユーザー関連のビジネスロジックを提供するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * ユーザーを作成するメソッド
	 * @param request
	 * @return User
	 */
	public UserEntity create(UserCreateRequestDTO request) {

		// emailの重複チェック
		if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
			throw new UserException(ApiErrorStatus.EMAIL_ALREADY_EXISTS);
		}

		UserEntity userEntity = UserEntity.builder()
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.R003)
				.build();
		return userRepository.save(userEntity);
	}
}
