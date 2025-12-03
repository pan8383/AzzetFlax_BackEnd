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
public class RentalHistoryDetailResponseDTO {
	private UUID rentalId;
	private UUID assetId;
	private UUID unitId;
	private String name;
	private String model;
	private String manufacturer;
	private Date due;
	private LocalDateTime returnAt;
	private String status;
	private String remarks;
	private LocalDateTime createdAt;

	public static RentalHistoryDetailResponseDTO from(Rental r) {
		return new RentalHistoryDetailResponseDTO(
				r.getRentalId(),
				r.getAsset().getAssetId(),
				r.getAssetUnit().getUnitId(),
				r.getAsset().getName(),
				r.getAsset().getModel(),
				r.getAsset().getManufacturer(),
				r.getDue(),
				r.getReturnAt(),
				r.getStatus().name(),
				r.getRemarks(),
				r.getCreatedAt());
	}
}
