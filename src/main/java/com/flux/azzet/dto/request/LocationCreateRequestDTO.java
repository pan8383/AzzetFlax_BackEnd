package com.flux.azzet.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationCreateRequestDTO {
	@NotBlank
	@Size(max = 4)
	private final String locationCode;

	@NotBlank
	@Size(max = 50)
	private final String name;

	@Size(max = 4)
	private final String parentCode;

	@Min(0)
	private final Integer sortOrder;
}