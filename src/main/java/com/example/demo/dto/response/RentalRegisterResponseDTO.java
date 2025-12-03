package com.example.demo.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalRegisterResponseDTO {
	private UUID rentalId;
	private UUID assetId;
	private boolean success;
	private String errorMessage;
}
