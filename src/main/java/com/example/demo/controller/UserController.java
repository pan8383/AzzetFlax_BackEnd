package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.UserCreateRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService usersService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponseDTO<Void>> register(@Valid @RequestBody UserCreateRequestDTO request) {
		usersService.create(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));

	}
}
