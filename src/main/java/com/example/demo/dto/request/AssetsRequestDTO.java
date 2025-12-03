package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AssetsRequestDTO {
	
	@NotBlank
	@Size(max = 50)
	private final String name;
	
	@NotBlank
	@Size(max = 50)
	private final String categoryCode;

	@NotBlank
	@Size(max = 50)
	private final String model;
	
	@NotNull
	@Min(0)
	private final Integer stock;
}