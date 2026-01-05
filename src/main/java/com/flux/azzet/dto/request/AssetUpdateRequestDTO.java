package com.flux.azzet.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetUpdateRequestDTO {

	@NotNull
	private UUID assetId;

	@Size(max = 100)
	private String name;

	@Size(max = 4)
	private String categoryCode;

	@Size(max = 50)
	private String model;

	@Size(max = 100)
	private String manufacturer;

	private Boolean isDeleted;
}
