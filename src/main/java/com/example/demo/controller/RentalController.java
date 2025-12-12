package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.demo.dto.response.RentalCreateResponseDTO;
import com.example.demo.dto.response.RentalHistoryDetailResponseDTO;
import com.example.demo.dto.response.RentalHistoryResponseDTO;
import com.example.demo.dto.response.RentalReturnResponseDTO;
import com.example.demo.model.RentalStatus;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.RentalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rental")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RentalController {

	private final RentalService rentalService;

	/**
	 * 借りる
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<ApiResponseDTO<List<RentalCreateResponseDTO>>> register(
			@Valid @RequestBody List<RentalCreateRequestDTO> requests,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<RentalCreateResponseDTO> data = rentalService.rentals(requests, userDetails);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}

	/**
	 * 返す
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/return")
	public ResponseEntity<ApiResponseDTO<List<RentalReturnResponseDTO>>> returnRental(
			@Valid @RequestBody List<RentalReturnRequestDTO> requests,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<RentalReturnResponseDTO> data = rentalService.returnUnits(requests, userDetails);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}

	@GetMapping("/history")
	public ResponseEntity<PageResponseDTO<RentalHistoryResponseDTO>> getHistories(
			@RequestParam(required = false) List<RentalStatus> statuses,
			Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		PageResponseDTO<RentalHistoryResponseDTO> response = rentalService
				.getRentalHistories(statuses, userDetails, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/history/{rentalId}")
	public ResponseEntity<ApiResponseDTO<RentalHistoryDetailResponseDTO>> getHistoryDetail(
			@PathVariable UUID rentalId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		RentalHistoryDetailResponseDTO data = rentalService.getRentalHistoryDetail(userDetails, rentalId);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}
}
