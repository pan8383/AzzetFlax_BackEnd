package com.flux.azzet.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.flux.azzet.model.RentalUnitStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalUnitResponseDTO {
	private UUID rentalUnitId;
	private UUID unitId;
	private RentalUnitStatus rentalUnitStatus;
	private LocalDateTime rentedAt;
	private LocalDateTime returnedAt;
}
