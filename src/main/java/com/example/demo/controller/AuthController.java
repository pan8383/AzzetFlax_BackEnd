package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AuthRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

	/**
	 * ログイン
	 * ヘッダーにCookieをセットする
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
			@RequestBody AuthRequestDTO request,
			HttpServletResponse response) {
		AuthResponseDTO userInfo = authService.getUserInfoWithSetToken(request, response);
		return ResponseEntity.ok(ApiResponseDTO.success(userInfo));
	}

	/**
	 * ログアウト
	 * ヘッダーのCookieを削除する
	 * @param response
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		authService.deleteCookieToken(response);
		return ResponseEntity.ok().build();
	}
}