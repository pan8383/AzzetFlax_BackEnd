package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AssetCreateRequestDTO;
import com.example.demo.dto.request.AssetUnitCreateRequestDTO;
import com.example.demo.dto.response.AssetResponseDTO;
import com.example.demo.dto.response.PageResponseDTO;
import com.example.demo.entity.AssetEntity;
import com.example.demo.entity.AssetUnitEntity;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.LocationEntity;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.exception.AssetException;
import com.example.demo.exception.CategoryException;
import com.example.demo.exception.LocationException;
import com.example.demo.mapper.PageMapper;
import com.example.demo.model.AssetUnitStatus;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.AssetUnitRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssetService {

	private final AssetRepository assetsRepository;
	private final AssetUnitRepository assetUnitRepository;
	private final CategoryRepository categoryRepository;
	private final LocationRepository locationRepository;

	/**
	 * Asset一覧からデータを検索（キーワード検索・カテゴリー検索）し、ページングされた結果を返す。
	 * @param keyword
	 * @param categoryCode
	 * @param pageable
	 * @return ページングレスポンス
	 */
	public PageResponseDTO<AssetResponseDTO> getAssets(String keyword, String categoryCode, Pageable pageable) {
		Page<AssetEntity> assetsPage = assetsRepository.searchAssets(keyword, categoryCode, pageable);
		Page<AssetResponseDTO> page = assetsPage.map(AssetResponseDTO::from);
		return PageMapper.toDTO(page);
	}

	/**
	 * リクエスト情報から新しいAssetをDBに生成する。
	 * @param request
	 * @return Asset
	 * @throws CategoryException カテゴリコードが存在しない場合に発生
	 */
	public AssetEntity createAsset(AssetCreateRequestDTO request) {
		CategoryEntity category = categoryRepository.findById(request.getCategoryCode())
				.orElseThrow(() -> new CategoryException(ApiErrorStatus.CATEGORY_NOT_FOUND));

		// アセットのオブジェクトを生成
		AssetEntity asset = AssetEntity.builder()
				.name(request.getName())
				.categoryEntity(category)
				.model(request.getModel())
				.manufacturer(request.getManufacturer())
				.build();
		return assetsRepository.save(asset);
	}

	/**
	 * リクエスト情報から新しいUnitをDBに生成する。単体
	 * @param request
	 * @return AssetUnit
	 * @throws AssetException アセットが存在しない場合に発生
	 * @throws LocationException ロケーションが存在しない場合に発生
	 */
	public AssetUnitEntity createUnit(AssetUnitCreateRequestDTO request) {

		// アセットをDBから取得
		AssetEntity assetEntity = assetsRepository.findById(request.getAssetId())
				.orElseThrow(() -> new AssetException(ApiErrorStatus.ASSET_NOT_FOUND));

		// ロケーションをDBから取得
		LocationEntity locationEntity = locationRepository.findById(request.getLocationCode())
				.orElseThrow(() -> new LocationException(ApiErrorStatus.LOCATION_NOT_FOUND));

		// ユニットのオブジェクトを生成
		AssetUnitEntity assetUnit = AssetUnitEntity.builder()
				.assetEntity(assetEntity)
				.serialNumber(request.getSerialNumber())
				.status(AssetUnitStatus.AVAILABLE)
				.locationEntity(locationEntity)
				.purchaseDate(request.getPurchaseDate())
				.purchasePrice(request.getPurchasePrice())
				.remarks(request.getRemarks())
				.build();
		return assetUnitRepository.save(assetUnit);
	}
}
