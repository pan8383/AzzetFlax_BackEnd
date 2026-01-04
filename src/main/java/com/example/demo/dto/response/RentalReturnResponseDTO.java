package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.model.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalReturnResponseDTO {
	private UUID rentalId;
	private RentalStatus status;
	private int returnedUnitCount;
	private int totalUnitCount;
}
