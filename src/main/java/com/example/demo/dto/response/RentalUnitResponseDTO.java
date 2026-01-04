package com.example.demo.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.model.RentalUnitStatus;

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
