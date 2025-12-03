package com.example.demo.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.CategoryRequestDTO;
import com.example.demo.dto.response.CategoryResponseDTO;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.exception.CategoriesException;
import com.example.demo.model.Category;
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
	public Category create(CategoryRequestDTO request) {

		// isDeleted=trueで同じ名前が存在した場合falseにする
		Category existing = categoryRepository.findByNameAndIsDeletedTrue(request.getName());
		if (existing != null) {
			// 削除フラグを解除
			existing.setIsDeleted(false);
			return categoryRepository.save(existing);
		}

		// 新規作成
		Category categories = Category.builder()
				.name(request.getName())
				.isDeleted(false)
				.build();

		try {
			return categoryRepository.save(categories);
		} catch (DataIntegrityViolationException ex) {
			throw new CategoriesException(ApiErrorStatus.CATEGORIES_ALREADY_EXISTS);
		} catch (Exception ex) {
			throw new CategoriesException(ApiErrorStatus.CATEGORIES_CREATE_FAILED);
		}
	}

	/**
	 * カテゴリーを作成するメソッド
	 * @param request
	 * @return
	 */
	public void delete(CategoryRequestDTO request) {
		Category category = categoryRepository.findById(request.getName())
				.orElseThrow(() -> new CategoriesException(ApiErrorStatus.CATEGORIES_NOT_FOUND));
		category.setIsDeleted(true);
		try {
			categoryRepository.save(category);
		} catch (Exception e) {
			throw new CategoriesException(ApiErrorStatus.CATEGORIES_DELETE_FAILED);
		}
	}
}
