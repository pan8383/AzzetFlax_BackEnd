package com.flux.azzet.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.flux.azzet.entity.AssetEntity;
import com.flux.azzet.entity.AssetUnitEntity;
import com.flux.azzet.entity.LocationEntity;
import com.flux.azzet.model.UnitStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetUnitDetailResponseDTO {
	private UUID unitId;
	private String serialNumber;
	private UnitStatus status;
	private Date purchaseDate;
	private BigDecimal purchasePrice;
	private String remarks;

	private String locationCode;
	private String locationName;

	private UUID assetId;
	private String name;
	private String categoryCode;
	private String categoryName;
	private String model;
	private String manufacturer;

	public static AssetUnitDetailResponseDTO from(
			AssetUnitEntity unit,
			LocationEntity locationEntity,
			AssetEntity assetEntity) {
		return new AssetUnitDetailResponseDTO(
				unit.getUnitId(),
				unit.getSerialNumber(),
				unit.getStatus(),
				unit.getPurchaseDate(),
				unit.getPurchasePrice(),
				unit.getRemarks(),
				locationEntity.getLocationCode(),
				locationEntity.getName(),
				assetEntity.getAssetId(),
				assetEntity.getName(),
				assetEntity.getCategoryEntity().getCategoryCode(),
				assetEntity.getCategoryEntity().getName(),
				assetEntity.getModel(),
				assetEntity.getManufacturer());
	}
}
