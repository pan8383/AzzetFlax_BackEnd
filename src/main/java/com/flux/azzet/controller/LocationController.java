package com.flux.azzet.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flux.azzet.dto.request.LocationCreateRequestDTO;
import com.flux.azzet.dto.request.LocationDeleteRequestDTO;
import com.flux.azzet.dto.response.ApiResponseDTO;
import com.flux.azzet.dto.response.LocationResponseDTO;
import com.flux.azzet.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {

	private final LocationService locationService;

	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<LocationResponseDTO>>> getLocations() {
		List<LocationResponseDTO> response = locationService.getLocations();
		return ResponseEntity.ok(ApiResponseDTO.success(response));
	}

	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody LocationCreateRequestDTO request) {
		locationService.create(request);
		return ResponseEntity.ok().build();
	}

	@PatchMapping
	public ResponseEntity<Void> delete(@Valid @RequestBody LocationDeleteRequestDTO request) {
		locationService.delete(request);
		return ResponseEntity.ok().build();
	}
}