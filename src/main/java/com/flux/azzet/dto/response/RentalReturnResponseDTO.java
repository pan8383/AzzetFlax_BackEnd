package com.flux.azzet.dto.response;

import java.util.UUID;

import com.flux.azzet.model.RentalStatus;

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
