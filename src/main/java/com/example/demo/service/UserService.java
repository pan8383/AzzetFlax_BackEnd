package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.exception.UsersException;
import com.example.demo.model.User;
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
	public User create(UserRequestDTO request) {

		if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
			throw new UsersException(ApiErrorStatus.EMAIL_ALREADY_EXISTS);
		}

		User user = User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.roleCode(request.getRoleCode())
				.build();
		return userRepository.save(user);
	}
}
