package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.model.Asset;
import com.example.demo.model.AssetUnit;

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

	public static AssetUnitDetailResponseDTO from(AssetUnit unit, Asset asset) {
		return new AssetUnitDetailResponseDTO(
				unit.getUnitId(),
				unit.getSerialNumber(),
				unit.getStatus().name(),
				unit.getLocation(),
				asset.getAssetId(),
				asset.getName(),
				asset.getModel(),
				asset.getManufacturer());
	}
}