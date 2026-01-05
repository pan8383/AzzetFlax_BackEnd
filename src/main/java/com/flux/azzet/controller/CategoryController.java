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

import com.flux.azzet.dto.request.CategoryCreateRequestDTO;
import com.flux.azzet.dto.request.CategoryDeleteRequestDTO;
import com.flux.azzet.dto.response.ApiResponseDTO;
import com.flux.azzet.dto.response.CategoryResponseDTO;
import com.flux.azzet.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

	private final CategoryService categoriesService;

	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getCategory() {
		List<CategoryResponseDTO> response = categoriesService.getCategories();
		return ResponseEntity.ok(ApiResponseDTO.success(response));
	}

	@PostMapping
	public ResponseEntity<ApiResponseDTO<Object>> create(@Valid @RequestBody CategoryCreateRequestDTO request) {
		categoriesService.create(request);
		return ResponseEntity.ok().build();
	}

	@PatchMapping
	public ResponseEntity<ApiResponseDTO<Object>> delete(@Valid @RequestBody CategoryDeleteRequestDTO request) {
		categoriesService.delete(request);
		return ResponseEntity.ok().build();
	}
}