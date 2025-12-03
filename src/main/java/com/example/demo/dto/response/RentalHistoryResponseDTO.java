package com.example.demo.dto.response;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.model.Rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalHistoryResponseDTO {
	private UUID rentalId;
	private UUID assetId;
	private UUID unitId;
	private String name;
	private Date due;
	private LocalDateTime returnAt;
	private String status;
	private LocalDateTime createdAt;

	public static RentalHistoryResponseDTO from(Rental r) {
		return new RentalHistoryResponseDTO(
				r.getRentalId(),
				r.getAsset().getAssetId(),
				r.getAssetUnit().getUnitId(),
				r.getAsset().getName(),
				r.getDue(),
				r.getReturnAt(),
				r.getStatus().name(),
				r.getCreatedAt());
	}
}
