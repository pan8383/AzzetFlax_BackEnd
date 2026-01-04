package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AuthRequestDTO;
import com.example.demo.dto.request.UserCreateRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.AuthLoginResponseDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;
	private final UserService usersService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponseDTO<Void>> create(@Valid @RequestBody UserCreateRequestDTO request) {
		usersService.create(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));

	}

	/**
	 * アクセストークンとリフレッシュトークンを発行
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<AuthLoginResponseDTO>> login(
			@RequestBody AuthRequestDTO request,
			HttpServletResponse response) {
		AuthLoginResponseDTO dto = authService.login(request, response);
		return ResponseEntity.ok(ApiResponseDTO.success(dto));
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		authService.logout(response);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/refresh")
	public ResponseEntity<ApiResponseDTO<Void>> refresh(
			@CookieValue("refresh_token") String refreshToken,
			HttpServletResponse response) {
		authService.refreshAccessToken(response, refreshToken);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}

}