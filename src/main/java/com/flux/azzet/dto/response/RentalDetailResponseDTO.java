package com.flux.azzet.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.flux.azzet.model.RentalUnitStatus;
import com.flux.azzet.model.UnitStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDetailResponseDTO {
	// Asset
	private UUID assetId;
	private String name;
	private String categoryCode;
	private String categoryName;
	private String model;
	private String manufacturer;
	
	// Location
	private String locationCode;
	private String locationName;
	
	// AssetUnit
	private UUID unitId;
	private String serialNumber;
	private UnitStatus status;
	private Date purchaseDate;
	private BigDecimal purchasePrice;
	private String remarks;

	// RentalUnit
	private UUID rentalUnitId;
	private RentalUnitStatus rentalUnitStatus;
	private LocalDateTime rentedAt;
	private LocalDateTime returnedAt;
}