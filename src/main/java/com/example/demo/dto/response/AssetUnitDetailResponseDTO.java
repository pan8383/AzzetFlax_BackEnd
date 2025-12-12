package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.entity.AssetEntity;
import com.example.demo.entity.AssetUnitEntity;
import com.example.demo.entity.LocationEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetUnitDetailResponseDTO {
	private UUID unitId;
	private String serialNumber;
	private String status;
	private String locationCode;

	private UUID assetId;
	private String name;
	private String model;
	private String manufacturer;

	public static AssetUnitDetailResponseDTO from(
			AssetUnitEntity unit,
			LocationEntity locationEntity,
			AssetEntity assetEntity) {
		return new AssetUnitDetailResponseDTO(
				unit.getUnitId(),
				unit.getSerialNumber(),
				unit.getStatus().name(),
				locationEntity.getLocationCode(),
				assetEntity.getAssetId(),
				assetEntity.getName(),
				assetEntity.getModel(),
				assetEntity.getManufacturer());
	}
}