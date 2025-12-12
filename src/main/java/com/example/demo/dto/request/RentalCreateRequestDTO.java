package com.example.demo.dto.request;

import java.sql.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentalCreateRequestDTO {
	@NotBlank
	private UUID assetId;

	@NotBlank
	private Integer quantity;

	@NotBlank
	private Date due;

	@NotNull
	private String remarks;
}
