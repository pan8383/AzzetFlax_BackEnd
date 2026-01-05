package com.flux.azzet.mapper;

import org.springframework.data.domain.Page;

import com.flux.azzet.dto.response.PageResponseDTO;

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