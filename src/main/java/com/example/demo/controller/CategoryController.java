package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.CategoryRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.CategoryResponseDTO;
import com.example.demo.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CategoryController {

	private final CategoryService categoriesService;

	@GetMapping("/get")
	public ResponseEntity<ApiResponseDTO<List<CategoryResponseDTO>>> getCategories() {
		List<CategoryResponseDTO> response = categoriesService.getCategories();
		return ResponseEntity.ok(ApiResponseDTO.success(response));
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody CategoryRequestDTO request) {
		categoriesService.create(request);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/delete")
	public ResponseEntity<Void> delete(@Valid @RequestBody CategoryRequestDTO request) {
		categoriesService.delete(request);
		return ResponseEntity.ok().build();
	}
}