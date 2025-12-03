package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AssetsRequestDTO;
import com.example.demo.dto.response.AssetResponseDTO;
import com.example.demo.model.Asset;
import com.example.demo.model.Category;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssetService {

	private final AssetRepository assetsRepository;
	private final CategoryRepository categoryRepository;

	public Page<AssetResponseDTO> getAssets(String keyword, String categoryCode, Pageable pageable) {
		Page<Asset> assetsPage = assetsRepository.searchAssets(keyword, categoryCode, pageable);
		return assetsPage.map(AssetResponseDTO::from);
	}

	/**
	 * アセットを作成するメソッド
	 * @param request
	 * @return 戻り値なし。
	 */
	public Asset create(AssetsRequestDTO request) {
		Category category = categoryRepository.findById(request.getCategoryCode())
				.orElseThrow(() -> new IllegalArgumentException("カテゴリが見つかりません: " + request.getCategoryCode()));

		Asset asset = Asset.builder()
				.name(request.getName())
				.category(category)
				.model(request.getModel())
				.build();
		return assetsRepository.save(asset);
	}

}
