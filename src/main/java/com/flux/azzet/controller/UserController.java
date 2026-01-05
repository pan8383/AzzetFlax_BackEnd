package com.flux.azzet.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flux.azzet.dto.request.UserPatchRequestDTO;
import com.flux.azzet.dto.response.ApiResponseDTO;
import com.flux.azzet.dto.response.PageResponseDTO;
import com.flux.azzet.dto.response.UserResponseDTO;
import com.flux.azzet.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService usersService;

	/**
	 * ユーザー一覧をページャーで取得する
	 * @param search
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PageResponseDTO<UserResponseDTO>> getUsers(
			@RequestParam(required = false, defaultValue = "") String search,
			Pageable pageable) {
		PageResponseDTO<UserResponseDTO> response = usersService.getUsers(search, pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * ユーザー情報を更新する パッチ
	 * @param request
	 * @return
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<Void>> updateUser(
			@PathVariable UUID id,
			@Valid @RequestBody UserPatchRequestDTO request) {
		usersService.patchUser(id, request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}

}
