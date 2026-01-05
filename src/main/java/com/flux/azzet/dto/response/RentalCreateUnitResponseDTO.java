package com.flux.azzet.dto.response;

import java.util.UUID;

import com.flux.azzet.entity.AssetUnitEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RentalCreateUnitResponseDTO {
	private UUID unitId;
	private boolean success;
	private String errorMessage;

	public static RentalCreateUnitResponseDTO success(AssetUnitEntity unit) {
		return new RentalCreateUnitResponseDTO(
				unit.getUnitId(),
				true,
				null);
	}

	public static RentalCreateUnitResponseDTO failure(UUID assetId, String errorMessage) {
		return new RentalCreateUnitResponseDTO(
				null,
				false,
				errorMessage);
	}
}