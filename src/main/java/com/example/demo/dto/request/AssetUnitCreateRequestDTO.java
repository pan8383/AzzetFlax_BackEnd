package com.example.demo.dto.request;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetUnitCreateRequestDTO {
	@NotNull
	private UUID assetId;

	@NotBlank
	@Size(max = 100)
	private final String serialNumber;

	@NotBlank
	@Size(max = 100)
	private final String locationCode;

	@NotNull
	private final Date purchaseDate;

	@NotNull
	@Positive
	private final BigDecimal purchasePrice;

	@Size(max = 500)
	private final String remarks;
}
