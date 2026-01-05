package com.flux.azzet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetCreateRequestDTO {
	
	@NotBlank
	@Size(max = 100)
	private final String name;
	
	@NotBlank
	@Size(max = 4)
	private final String categoryCode;

	@NotBlank
	@Size(max = 50)
	private final String model;
	
	@NotBlank
	@Size(max = 100)
	private final String manufacturer;
	

}