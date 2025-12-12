package com.example.demo.dto.response;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.entity.RentalEntity;

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

	public static RentalHistoryDetailResponseDTO from(RentalEntity r) {
		return new RentalHistoryDetailResponseDTO(
				r.getRentalId(),
				r.getAssetEntity().getAssetId(),
				r.getAssetUnit().getUnitId(),
				r.getAssetEntity().getName(),
				r.getAssetEntity().getModel(),
				r.getAssetEntity().getManufacturer(),
				r.getDue(),
				r.getReturnAt(),
				r.getStatus().name(),
				r.getRemarks(),
				r.getCreatedAt());
	}
}
