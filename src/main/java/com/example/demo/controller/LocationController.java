package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.LocationCreateRequestDTO;
import com.example.demo.dto.request.LocationDeleteRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.LocationResponseDTO;
import com.example.demo.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {

	private final LocationService locationService;

	@GetMapping("/get")
	public ResponseEntity<ApiResponseDTO<List<LocationResponseDTO>>> getCategories() {
		List<LocationResponseDTO> response = locationService.getLocations();
		return ResponseEntity.ok(ApiResponseDTO.success(response));
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody LocationCreateRequestDTO request) {
		locationService.create(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/delete")
	public ResponseEntity<Void> delete(@Valid @RequestBody LocationDeleteRequestDTO request) {
		locationService.delete(request);
		return ResponseEntity.ok().build();
	}
}