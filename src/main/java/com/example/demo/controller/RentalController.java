package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dto.request.RentalRegisterRequestDTO;
import com.example.demo.dto.request.RentalReturnRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.PageResponseDTO;
import com.example.demo.dto.response.RentalHistoryDetailResponseDTO;
import com.example.demo.dto.response.RentalHistoryResponseDTO;
import com.example.demo.dto.response.RentalRegisterResponseDTO;
import com.example.demo.dto.response.RentalReturnResponseDTO;
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
	public ResponseEntity<ApiResponseDTO<List<RentalRegisterResponseDTO>>> register(
			@Valid @RequestBody List<RentalRegisterRequestDTO> requests,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (requests == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		List<RentalRegisterResponseDTO> data = rentalService.rentals(requests, userDetails);
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

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		if (requests == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		List<RentalReturnResponseDTO> data = rentalService.returnUnits(requests, userDetails);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}

	@GetMapping("/history")
	public ResponseEntity<PageResponseDTO<RentalHistoryResponseDTO>> getHistories(
			//@RequestParam(required = false, defaultValue = "") String search,
			Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Page<RentalHistoryResponseDTO> rentalHistoryPages = rentalService.getRentalHistories(userDetails, pageable);
		PageResponseDTO<RentalHistoryResponseDTO> response = new PageResponseDTO<RentalHistoryResponseDTO>(
				rentalHistoryPages.getContent(),
				rentalHistoryPages.getNumber(),
				rentalHistoryPages.getSize(),
				rentalHistoryPages.getTotalElements(),
				rentalHistoryPages.getTotalPages());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/history/{rentalId}")
	public ResponseEntity<ApiResponseDTO<RentalHistoryDetailResponseDTO>> getHistoryDetail(
			@PathVariable UUID rentalId,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		RentalHistoryDetailResponseDTO data = rentalService.getRentalHistoryDetail(userDetails, rentalId);
		return ResponseEntity.ok(ApiResponseDTO.success(data));
	}
}
