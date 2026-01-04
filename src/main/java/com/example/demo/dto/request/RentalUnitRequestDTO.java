package com.example.demo.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentalUnitRequestDTO {
	@NotNull(message = "assetIdは必須です")
	private UUID assetId;

	@Min(value = 1, message = "quantityは1以上で指定してください")
	private int quantity;
}
