package com.flux.azzet.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.flux.azzet.entity.RentalEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RentalCreateResponseDTO {
	private UUID rentalId;
	private LocalDate expectedReturnDate;
	private List<RentalCreateAssetResponseDTO> assetResponses;

	public static RentalCreateResponseDTO from(RentalEntity rental, List<RentalCreateAssetResponseDTO> assetResponses) {
		return new RentalCreateResponseDTO(rental.getRentalId(), rental.getExpectedReturnDate(), assetResponses);
	}
}