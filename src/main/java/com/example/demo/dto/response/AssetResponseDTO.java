package com.example.demo.dto.response;

import java.util.UUID;

import com.example.demo.entity.AssetEntity;
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

	public static AssetResponseDTO from(AssetEntity asset) {
		return AssetResponseDTO.builder()
				.assetId(asset.getAssetId())
				.name(asset.getName())
				.categoryCode(asset.getCategoryEntity().getCategoryCode())
				.categoryName(asset.getCategoryEntity().getName())
				.model(asset.getModel())
				.manufacturer(asset.getManufacturer())
				.isAvailable(asset.getAssetUnitEntities()
						.stream()
						.anyMatch(u -> !u.getIsDeleted() && u.getStatus() == AssetUnitStatus.AVAILABLE))
				.totalStock((long) asset.getAssetUnitEntities().size())
				.availableStock(asset.getAssetUnitEntities()
						.stream()
						.filter(u -> !u.getIsDeleted() && u.getStatus() == AssetUnitStatus.AVAILABLE)
						.count())
				.build();
	}
}