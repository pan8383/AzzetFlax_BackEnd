package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.model.Asset;
import com.example.demo.model.AssetUnitStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponseDTO {
	private UUID assetId;
	private String name;
	private String categoryCode;
	private String categoryName;
	private String model;
	private String manufacturer;
	private Boolean isAvailable;
	private Long totalStock;
	private Long availableStock;

	public static AssetResponseDTO from(Asset asset) {
		return AssetResponseDTO.builder()
				.assetId(asset.getAssetId())
				.name(asset.getName())
				.categoryCode(asset.getCategory().getCategoryCode())
				.categoryName(asset.getCategory().getName())
				.model(asset.getModel())
				.manufacturer(asset.getManufacturer())
				.isAvailable(asset.getAssetUnits()
						.stream()
						.anyMatch(u -> !u.getIsDeleted() && u.getStatus() == AssetUnitStatus.AVAILABLE))
				.totalStock((long) asset.getAssetUnits().size())
				.availableStock(asset.getAssetUnits()
						.stream()
						.filter(u -> !u.getIsDeleted() && u.getStatus() == AssetUnitStatus.AVAILABLE)
						.count())
				.build();
	}
}