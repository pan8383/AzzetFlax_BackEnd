package com.example.demo.dto.request;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssetUnitUpdateRequestDTO {

	@NotNull
	private UUID unitId;

	@Size(max = 100)
	private String serialNumber;

	private String status;

	@Size(max = 4)
	private String locationCode;

	private Date purchaseDate;

	@PositiveOrZero
	private BigDecimal purchasePrice;

	@Size(max = 500)
	private String remarks;
}
