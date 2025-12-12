package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AssetCreateRequestDTO;
import com.example.demo.dto.request.AssetUnitCreateRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.AssetResponseDTO;
import com.example.demo.dto.response.PageResponseDTO;
import com.example.demo.service.AssetService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/assets")
@RequiredArgsConstructor
@RestController
public class AssetController {

	private final AssetService assetsService;

	/**
	 * Assetをページャーで取得する
	 * @param search
	 * @param categoryCode
	 * @param pageable
	 * @return
	 */
	@GetMapping("/get")
	public ResponseEntity<PageResponseDTO<AssetResponseDTO>> getAssets(
			@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false, defaultValue = "") String categoryCode,
			Pageable pageable) {
		PageResponseDTO<AssetResponseDTO> response = assetsService.getAssets(search, categoryCode, pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * Assetを登録する
	 * @param request
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<ApiResponseDTO<Void>> createAsset(
			@Valid @RequestBody AssetCreateRequestDTO request) {
		assetsService.createAsset(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}

	/**
	 * AssetUnitを登録する
	 * @param request
	 * @return
	 */
	@PostMapping("/register-unit")
	public ResponseEntity<ApiResponseDTO<Void>> createUnit(
			@Valid @RequestBody AssetUnitCreateRequestDTO request) {
		assetsService.createUnit(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}
}
