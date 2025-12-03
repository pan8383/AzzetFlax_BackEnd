package com.example.demo.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class RentalReturnRequestDTO {
	@NonNull
	private UUID rentalId;
	
	@NonNull
	private UUID unitId;
}

