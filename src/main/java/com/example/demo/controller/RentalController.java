package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.RentalCreateRequestDTO;
import com.example.demo.dto.request.RentalReturnRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.PageResponseDTO;
import com.example.demo.dto.response.RentalAssetsListResponseDTO;
import com.example.demo.dto.response.RentalCreateResponseDTO;
import com.example.demo.dto.response.RentalDetailResponseDTO;
import com.example.demo.dto.response.RentalReturnResponseDTO;
import com.example.demo.model.RentalStatus;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.RentalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets/rentals")
public class RentalController {

	private final RentalService rentalService;

	@GetMapping
	public ResponseEntity<PageResponseDTO<RentalAssetsListResponseDTO>> getRentalLists(
			@RequestParam(required = false) List<RentalStatus> statuses,
			Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		PageResponseDTO<RentalAssetsListResponseDTO> response = rentalService
				.getRentalAssetList(statuses, userDetails, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{rentalId}")
	public ResponseEntity<ApiResponseDTO<List<RentalDetailResponseDTO>>> getRentalListDetails(
			@PathVariable UUID rentalId) {
		ApiResponseDTO<List<RentalDetailResponseDTO>> response = rentalService.getRentalListDetails(rentalId);
		return ResponseEntity.ok(response);
	}

	/**
	 * 借りる
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ApiResponseDTO<RentalCreateResponseDTO>> create(
			@Valid @RequestBody RentalCreateRequestDTO requests,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		RentalCreateResponseDTO data = rentalService.create(requests, userDetails);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}

	/**
	 * 返す
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@PatchMapping
	public ResponseEntity<ApiResponseDTO<List<RentalReturnResponseDTO>>> returnUnits(
			@Valid @RequestBody List<RentalReturnRequestDTO> requests,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<RentalReturnResponseDTO> data = rentalService.returnUnits(requests, userDetails);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}

}
