package com.example.demo.dto.response;

import com.example.demo.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
	private String categoryCode;
	private String name;
	private String parentCategoryCode;
	private Integer sortOrder;

	// 静的ファクトリーメソッド
	public static CategoryResponseDTO from(Category e) {
		return new CategoryResponseDTO(
				e.getCategoryCode(),
				e.getName(),
				e.getParentCategoryCode(),
				e.getSortOrder());
	}
}
