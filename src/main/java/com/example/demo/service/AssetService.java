package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AssetCreateRequestDTO;
import com.example.demo.dto.request.AssetUnitCreateRequestDTO;
import com.example.demo.dto.request.AssetUnitUpdateRequestDTO;
import com.example.demo.dto.request.AssetUpdateRequestDTO;
import com.example.demo.dto.response.ApiResponseDTO;
import com.example.demo.dto.response.AssetResponseDTO;
import com.example.demo.dto.response.AssetUnitDetailResponseDTO;
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
import com.example.demo.model.UnitStatus;
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

	// ==================================================
	//
	// assets
	//
	// ==================================================
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

		if (assetsRepository.existsByNameAndIsDeletedFalse(request.getName())) {
			throw new AssetException(ApiErrorStatus.ASSET_NAME_ALREADY_EXISTS);
		}

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
	 * Assetをアップデート
	 * @param request
	 */
	@Transactional
	public void updateAsset(AssetUpdateRequestDTO request) {

		AssetEntity asset = assetsRepository.findById(request.getAssetId())
				.orElseThrow(() -> new AssetException(ApiErrorStatus.ASSET_NOT_FOUND));

		// 名前
		if (request.getName() != null && !request.getName().isEmpty()) {
			asset.setName(request.getName());
		}

		// カテゴリー
		if (request.getCategoryCode() != null && !request.getCategoryCode().isEmpty()) {
			CategoryEntity category = categoryRepository.findById(request.getCategoryCode())
					.orElseThrow(() -> new LocationException(ApiErrorStatus.CATEGORY_NOT_FOUND));
			asset.setCategoryEntity(category);
		}

		// 型番
		if (request.getModel() != null && !request.getModel().isEmpty()) {
			asset.setModel(request.getModel());
		}

		// メーカー
		if (request.getManufacturer() != null && !request.getManufacturer().isEmpty()) {
			asset.setManufacturer(request.getManufacturer());
		}

		// 削除フラグ
		if (request.getIsDeleted() != null) {
			asset.setIsDeleted(request.getIsDeleted());
		}

		assetsRepository.save(asset);
	}

	public void deleteAsset(UUID assetId) {

		// アセットをDBから取得
		AssetEntity assetEntity = assetsRepository.findById(assetId)
				.orElseThrow(() -> new AssetException(ApiErrorStatus.ASSET_NOT_FOUND));

		assetEntity.setIsDeleted(true);

		assetsRepository.save(assetEntity);
	}

	// ==================================================
	//
	// units
	//
	// ==================================================

	public ApiResponseDTO<List<AssetUnitDetailResponseDTO>> getAssetUnits(UUID assetId) {
		List<AssetUnitDetailResponseDTO> units = assetUnitRepository.findAllActiveWithAssetUnitByAssetId(assetId);
		return ApiResponseDTO.success(units);
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
				.status(UnitStatus.AVAILABLE)
				.locationEntity(locationEntity)
				.purchaseDate(request.getPurchaseDate())
				.purchasePrice(request.getPurchasePrice())
				.remarks(request.getRemarks())
				.build();
		return assetUnitRepository.save(assetUnit);
	}

	/**
	 * ユニットをアップデート
	 * @param request
	 */
	@Transactional
	public void updateUnit(AssetUnitUpdateRequestDTO request) {

		AssetUnitEntity unit = assetUnitRepository.findById(request.getUnitId())
				.orElseThrow(() -> new AssetException(ApiErrorStatus.ASSET_UNIT_NOT_FOUND));

		// シリアル番号
		if (request.getSerialNumber() != null && !request.getSerialNumber().isEmpty()) {
			unit.setSerialNumber(request.getSerialNumber());
		}

		// ステータス
		if (request.getStatus() != null && !request.getStatus().isEmpty()) {
			unit.setStatus(UnitStatus.valueOf(request.getStatus()));
		}

		// ロケーション
		if (request.getLocationCode() != null && !request.getLocationCode().isEmpty()) {
			LocationEntity location = locationRepository.findById(request.getLocationCode())
					.orElseThrow(() -> new LocationException(ApiErrorStatus.LOCATION_NOT_FOUND));
			unit.setLocationEntity(location);
		}

		// 購入日
		if (request.getPurchaseDate() != null) {
			unit.setPurchaseDate(request.getPurchaseDate());
		}

		// 購入金額
		if (request.getPurchasePrice() != null) {
			unit.setPurchasePrice(request.getPurchasePrice());
		}

		// 備考
		if (request.getRemarks() != null && !request.getRemarks().isEmpty()) {
			unit.setRemarks(request.getRemarks());
		}

		assetUnitRepository.save(unit);
	}

	public void deleteUnit(UUID unitId) {

		// アセットをDBから取得
		AssetUnitEntity assetUnitEntity = assetUnitRepository.findById(unitId)
				.orElseThrow(() -> new AssetException(ApiErrorStatus.ASSET_UNIT_NOT_FOUND));

		assetUnitEntity.dispose();

		assetUnitRepository.save(assetUnitEntity);
	}

}
