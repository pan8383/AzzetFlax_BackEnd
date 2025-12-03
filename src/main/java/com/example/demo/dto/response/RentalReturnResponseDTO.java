package com.example.demo.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalReturnResponseDTO {
	private UUID rentalId;
	private boolean success;
	private String errorMessage;
}
