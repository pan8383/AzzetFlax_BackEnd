package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CategoryCreateRequestDTO;
import com.example.demo.dto.request.CategoryDeleteRequestDTO;
import com.example.demo.dto.response.CategoryResponseDTO;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.exception.CategoryException;
import com.example.demo.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	/**
	 * カテゴリーを取得するメソッド
	 * @return
	 */
	public List<CategoryResponseDTO> getCategories() {
		return categoryRepository.findAllActive()
				.stream()
				.map(CategoryResponseDTO::from)
				.toList();
	}

	/**
	 * カテゴリーを作成するメソッド
	 * @param request
	 * @return
	 */
	public CategoryEntity create(CategoryCreateRequestDTO request) {

		// 同じ名前で登録されていた場合、削除フラグを解除する
		Optional<CategoryEntity> optCategory = categoryRepository.findByName(request.getName());

		return optCategory.map(category -> {
			if (category.getIsDeleted()) {
				category.setIsDeleted(false);
				return categoryRepository.save(category);
			} else {
				throw new CategoryException(ApiErrorStatus.CATEGORY_NAME_ALREADY_EXISTS);
			}
		}).orElseGet(() -> {

			// コードが重複した場合例外を発生
			if (categoryRepository.existsById(request.getCategoryCode())) {
				throw new CategoryException(ApiErrorStatus.CATEGORY_CODE_ALREADY_EXISTS);
			}

			// 新規作成
			CategoryEntity newCategory = CategoryEntity.builder()
					.categoryCode(request.getCategoryCode())
					.name(request.getName())
					.parentCode(emptyToNull(request.getParentCode()))
					.sortOrder(nullToZero(request.getSortOrder()))
					.isDeleted(false)
					.build();
			return categoryRepository.save(newCategory);
		});
	}

	/**
	 * 空文字の場合は null に変換
	 */
	private String emptyToNull(String value) {
		return (value == null || value.isEmpty()) ? null : value;
	}

	/**
	 * nullの場合は 0 に変換
	 */
	private Integer nullToZero(Integer value) {
		return value != null ? value : 0;
	}

	/**
	 * カテゴリーを削除するメソッド
	 * @param request
	 * @return
	 */
	public void delete(CategoryDeleteRequestDTO request) {
		CategoryEntity category = categoryRepository.findById(request.getCategoryCode())
				.orElseThrow(() -> new CategoryException(ApiErrorStatus.CATEGORY_NOT_FOUND));
		category.setIsDeleted(true);
		categoryRepository.save(category);
	}
}
