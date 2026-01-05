package com.flux.azzet.dto.response;

import java.util.List;
import java.util.UUID;

import com.flux.azzet.dto.request.RentalUnitRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RentalCreateAssetResponseDTO {
	private UUID assetId;
	private int requestedQuantity;
	private List<RentalCreateUnitResponseDTO> units;

	public static RentalCreateAssetResponseDTO from(
			RentalUnitRequestDTO u,
			List<RentalCreateUnitResponseDTO> unitResponses) {
		return new RentalCreateAssetResponseDTO(
				u.getAssetId(),
				u.getQuantity(),
				unitResponses);
	}
}