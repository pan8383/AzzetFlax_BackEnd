package com.flux.azzet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flux.azzet.dto.request.CategoryCreateRequestDTO;
import com.flux.azzet.dto.request.CategoryDeleteRequestDTO;
import com.flux.azzet.dto.response.CategoryResponseDTO;
import com.flux.azzet.entity.CategoryEntity;
import com.flux.azzet.exception.ApiErrorStatus;
import com.flux.azzet.exception.CategoryException;
import com.flux.azzet.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	/**
	 * カテゴリーを取得する
	 * @return
	 */
	public List<CategoryResponseDTO> getCategories() {
		return categoryRepository.findAllActive()
				.stream()
				.map(CategoryResponseDTO::from)
				.toList();
	}

	/**
	 * カテゴリーを作成する
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
	 * カテゴリーを削除する
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
