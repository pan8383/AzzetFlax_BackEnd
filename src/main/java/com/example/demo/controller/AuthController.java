package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dto.request.AuthRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
			@RequestBody AuthRequestDTO request,
			HttpServletResponse response) {
		AuthResponseDTO userInfo = authService.getUserInfoWithSetToken(request, response);
		return ResponseEntity.ok(ApiResponseDTO.success(userInfo));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		authService.deleteCookieToken(response);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> getMe(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		AuthResponseDTO userInfo = authService.getValidateUser(userDetails);
		return ResponseEntity.ok(ApiResponseDTO.success(userInfo));
	}
}