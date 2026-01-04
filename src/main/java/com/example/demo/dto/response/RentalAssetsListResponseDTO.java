package com.example.demo.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.demo.model.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalAssetsListResponseDTO {
	private UUID rentalId;
	private UUID userId;
	private LocalDate rentalDate;
	private LocalDate expectedReturnDate;
	private LocalDate actualReturnDate;
	private RentalStatus status;
	private String remarks;
	private List<RentalUnitResponseDTO> units;
}