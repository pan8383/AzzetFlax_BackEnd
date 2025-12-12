package com.example.demo.mapper;

import org.springframework.data.domain.Page;

import com.example.demo.dto.response.PageResponseDTO;

/**
 * ページングレスポンス用ページマッパー
 */
public class PageMapper {
	public static <T> PageResponseDTO<T> toDTO(Page<T> page) {
		return PageResponseDTO.<T> builder()
				.content(page.getContent())
				.pageNumber(page.getNumber())
				.pageSize(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.build();
	}
}