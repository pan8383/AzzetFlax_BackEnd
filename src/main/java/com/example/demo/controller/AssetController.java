package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AssetsRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.AssetResponseDTO;
import com.example.demo.dto.response.PageResponseDTO;
import com.example.demo.service.AssetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AssetController {

	private final AssetService assetsService;

	@GetMapping("/get")
	public ResponseEntity<PageResponseDTO<AssetResponseDTO>> getAssets(
			@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false, defaultValue = "") String categoryCode,
			Pageable pageable) {
		Page<AssetResponseDTO> assetsPages = assetsService.getAssets(search, categoryCode, pageable);
		PageResponseDTO<AssetResponseDTO> response = new PageResponseDTO<>(
				assetsPages.getContent(),
				assetsPages.getNumber(),
				assetsPages.getSize(),
				assetsPages.getTotalElements(),
				assetsPages.getTotalPages());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponseDTO<Void>> register(@Valid @RequestBody AssetsRequestDTO request) {
		assetsService.create(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}
}
