package com.flux.azzet.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.flux.azzet.dto.request.UserCreateRequestDTO;
import com.flux.azzet.dto.request.UserPatchRequestDTO;
import com.flux.azzet.dto.response.PageResponseDTO;
import com.flux.azzet.dto.response.UserResponseDTO;
import com.flux.azzet.entity.UserEntity;
import com.flux.azzet.exception.ApiErrorStatus;
import com.flux.azzet.exception.UserException;
import com.flux.azzet.mapper.PageMapper;
import com.flux.azzet.model.Role;
import com.flux.azzet.repository.UserRepository;

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
				.role(Role.valueOf(request.getRole()))
				.build();
		return userRepository.save(userEntity);
	}

	/**
	 * ユーザー一覧からデータを検索（キーワード検索）し、ページングされた結果を返す。
	 * @param keyword
	 * @param pageable
	 * @return ページングレスポンス
	 */
	public PageResponseDTO<UserResponseDTO> getUsers(String keyword, Pageable pageable) {
		Page<UserEntity> usersPage = userRepository.searchUsers(keyword, pageable);
		Page<UserResponseDTO> page = usersPage.map(UserResponseDTO::from);
		return PageMapper.toDTO(page);
	}

	/**
	 * PATCHメソッドのリクエストでユーザー情報を部分更新する
	 * @param request
	 * @return AssetUnit
	 * @throws UserException
	 */
	public void patchUser(UUID userId, UserPatchRequestDTO request) {

		// ユーザーIDで存在チェック
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException(ApiErrorStatus.USER_NOT_FOUND));

		// name
		if (request.getName() != null) {
			user.setName(request.getName());
		}

		// email（重複チェック含む）
		if (request.getEmail() != null) {
			if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
				throw new UserException(ApiErrorStatus.EMAIL_ALREADY_EXISTS);
			}
			user.setEmail(request.getEmail());
		}

		// password（エンコード含む）
		if (request.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		// role
		if (request.getRole() != null) {
			user.setRole(request.getRole());
		}

		userRepository.save(user);
	}
}
