package com.example.demo.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentalReturnRequestDTO {

	@NotNull
	private UUID rentalId;

	@NotEmpty
	private List<@NotNull UUID> rentalUnitIds;
}
