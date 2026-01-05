package com.flux.azzet.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flux.azzet.dto.request.AssetCreateRequestDTO;
import com.flux.azzet.dto.request.AssetUnitCreateRequestDTO;
import com.flux.azzet.dto.request.AssetUnitUpdateRequestDTO;
import com.flux.azzet.dto.request.AssetUpdateRequestDTO;
import com.flux.azzet.dto.response.ApiResponseDTO;
import com.flux.azzet.dto.response.AssetResponseDTO;
import com.flux.azzet.dto.response.AssetUnitDetailResponseDTO;
import com.flux.azzet.dto.response.PageResponseDTO;
import com.flux.azzet.service.AssetService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/assets")
@RequiredArgsConstructor
@RestController
public class AssetController {

	private final AssetService assetsService;

	// ==================================================
	//
	// assets
	//
	// ==================================================

	/**
	 * GET Assetをページャーで取得する
	 */
	@GetMapping
	public ResponseEntity<PageResponseDTO<AssetResponseDTO>> getAssets(
			@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false, defaultValue = "") String categoryCode,
			Pageable pageable) {
		PageResponseDTO<AssetResponseDTO> response = assetsService.getAssets(search, categoryCode, pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST Assetを登録する
	 */
	@PostMapping
	public ResponseEntity<ApiResponseDTO<Void>> createAsset(
			@Valid @RequestBody AssetCreateRequestDTO request) {
		assetsService.createAsset(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}

	/**
	 * PATCH Assetを部分更新する
	 */
	@PatchMapping
	public ResponseEntity<ApiResponseDTO<Void>> updateAsset(@Valid @RequestBody AssetUpdateRequestDTO request) {
		assetsService.updateAsset(request);
		return ResponseEntity.ok().build();
	}

	/**
	 * DELETE Assetを削除する
	 */
	@DeleteMapping("/{assetId}")
	public ResponseEntity<ApiResponseDTO<Void>> deleteAsset(@PathVariable UUID assetId) {
		assetsService.deleteAsset(assetId);
		return ResponseEntity.ok().build();
	}

	// ==================================================
	//
	// units
	//
	// ==================================================
	/**
	 * GET AssetUnitを取得する
	 */
	@GetMapping("/{assetId}/units")
	public ResponseEntity<ApiResponseDTO<List<AssetUnitDetailResponseDTO>>> getAssetUnits(
			@PathVariable UUID assetId) {
		ApiResponseDTO<List<AssetUnitDetailResponseDTO>> response = assetsService.getAssetUnits(assetId);
		return ResponseEntity.ok(response);
	}

	/**
	 * POST AssetUnitを登録する
	 */
	@PostMapping("/{assetId}/units")
	public ResponseEntity<ApiResponseDTO<Void>> createUnit(
			@PathVariable UUID assetId,
			@Valid @RequestBody AssetUnitCreateRequestDTO request) {
		assetsService.createUnit(request);
		return ResponseEntity.ok(ApiResponseDTO.success(null));
	}

	/**
	 * PATCH AssetUnitを部分更新する
	 */
	@PatchMapping("/units")
	public ResponseEntity<ApiResponseDTO<Void>> updateUnit(@Valid @RequestBody AssetUnitUpdateRequestDTO request) {
		assetsService.updateUnit(request);
		return ResponseEntity.ok().build();
	}

	/**
	 * PATCH AssetUnitを削除する
	 */
	@DeleteMapping("/units/{unitId}")
	public ResponseEntity<ApiResponseDTO<Void>> deleteUnit(@PathVariable UUID unitId) {
		assetsService.deleteUnit(unitId);
		return ResponseEntity.ok().build();
	}
}
