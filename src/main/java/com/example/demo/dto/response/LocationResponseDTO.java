package com.example.demo.dto.response;

import com.example.demo.entity.LocationEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponseDTO {
	private String locationCode;
	private String name;
	private String parentCode;
	private Integer sortOrder;

	// 静的ファクトリーメソッド
	public static LocationResponseDTO from(LocationEntity e) {
		return new LocationResponseDTO(
				e.getLocationCode(),
				e.getName(),
				e.getParentCode(),
				e.getSortOrder());
	}
}
