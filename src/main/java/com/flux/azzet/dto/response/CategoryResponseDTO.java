package com.flux.azzet.dto.response;

import com.flux.azzet.entity.CategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
	private String categoryCode;
	private String name;
	private String parentCode;
	private Integer sortOrder;

	// 静的ファクトリーメソッド
	public static CategoryResponseDTO from(CategoryEntity e) {
		return new CategoryResponseDTO(
				e.getCategoryCode(),
				e.getName(),
				e.getParentCode(),
				e.getSortOrder());
	}
}
