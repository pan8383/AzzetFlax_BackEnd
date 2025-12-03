package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.dto.request.RentalRegisterRequestDTO;
import com.example.demo.dto.request.RentalReturnRequestDTO;
import com.example.demo.dto.response.RentalHistoryDetailResponseDTO;
import com.example.demo.dto.response.RentalHistoryResponseDTO;
import com.example.demo.dto.response.RentalRegisterResponseDTO;
import com.example.demo.dto.response.RentalReturnResponseDTO;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.model.AssetUnit;
import com.example.demo.model.AssetUnitStatus;
import com.example.demo.model.Rental;
import com.example.demo.model.RentalStatus;
import com.example.demo.repository.AssetUnitRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RentalService {

	private final UserRepository userRepository;
	private final RentalRepository rentalRepository;
	private final AssetUnitRepository assetUnitRepository;

	/**
	 * レンタル処理：ループ
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@Transactional
	public List<RentalRegisterResponseDTO> rentals(
			List<RentalRegisterRequestDTO> requests, CustomUserDetails userDetails) {

		// レスポンス用結果リストを作成
		List<RentalRegisterResponseDTO> resultList = new ArrayList<>();

		// assetの数だけループする
		for (RentalRegisterRequestDTO request : requests) {
			for (int i = 0; i < request.getQuantity(); i++) {
				resultList.add(rental(request, userDetails));
			}
		}
		return resultList;
	}

	/*
	 * レンタル処理：単体
	 * 【処理の流れ】
	 * １．１件だけデータ取得
	 * ２．rentalsに有効なデータが存在しないかチェック
	 * ３．unitsのステータスを利用中に変更
	 * ４．rentalsに登録
	 */
	private RentalRegisterResponseDTO rental(RentalRegisterRequestDTO request, CustomUserDetails userDetails) {

		// 利用可能な１件をロックして取得する
		Optional<AssetUnit> unitOpt = assetUnitRepository
				.findFirstByAssetAssetIdAndStatusAndIsDeletedFalseOrderByUnitIdAsc(
						request.getAssetId(),
						AssetUnitStatus.AVAILABLE);

		// ０件の場合、失敗にする
		if (unitOpt.isEmpty()) {
			return buildResponse(null, request.getAssetId(), false, ApiErrorStatus.RENTAL_UNIT_NOT_FOUND.getMessage());
		}

		AssetUnit unit = unitOpt.get();

		// ユニットを使用中更新する
		if (assetUnitRepository.updateStatus(unit.getUnitId(), AssetUnitStatus.IN_USE) < 0) {
			return buildResponse(null, request.getAssetId(), false, ApiErrorStatus.RENTAL_COMMIT_FAILED.getMessage());
		}

		// エンティティを作成
		Rental rental = Rental.builder()
				.asset(unit.getAsset())
				.assetUnit(unit)
				.user(userRepository.findById(userDetails.getUserId()).orElseThrow())
				.due(request.getDue())
				.status(RentalStatus.RENTED)
				.remarks(request.getRemarks())
				.build();

		// エンティティを登録
		Rental data = rentalRepository.save(rental);

		// レスポス用のデータにエンティティを追加（成功）
		return buildResponse(data.getRentalId(), data.getAsset().getAssetId(), true, null);
	}

	// ヘルパー
	private RentalRegisterResponseDTO buildResponse(UUID rentalId, UUID assetId, boolean success, String errorMessage) {
		return new RentalRegisterResponseDTO(rentalId, assetId, success, errorMessage);
	}

	/**
	 * 返却
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@Transactional
	public List<RentalReturnResponseDTO> returnUnits(
			List<RentalReturnRequestDTO> requests,
			CustomUserDetails userDetails) {

		// レスポンス用結果リストを作成
		List<RentalReturnResponseDTO> resultList = new ArrayList<>();

		// assetの数だけループする
		for (RentalReturnRequestDTO request : requests) {
			resultList.add(returnUnit(request, userDetails));
		}
		return resultList;
	}

	private RentalReturnResponseDTO returnUnit(RentalReturnRequestDTO request, CustomUserDetails userDetails) {

		// ユニットのステータスを返却済みにする
		if (assetUnitRepository.updateStatus(
				request.getUnitId(),
				AssetUnitStatus.AVAILABLE) < 0) {
			return RentalReturnResponseDTO.builder()
					.rentalId(request.getRentalId())
					.success(false)
					.errorMessage(ApiErrorStatus.RENTAL_RETURN_FAILED.getMessage())
					.build();
		}

		// レンタルのステータスを返却済みにする
		if (rentalRepository.updateStatus(
				userDetails.getUserId(),
				request.getRentalId(),
				LocalDateTime.now(),
				RentalStatus.RETURNED) < 0) {
			return RentalReturnResponseDTO.builder()
					.rentalId(request.getRentalId())
					.success(false)
					.errorMessage(ApiErrorStatus.RENTAL_RETURN_FAILED.getMessage())
					.build();

		}

		// レスポス用のデータにエンティティを追加（成功）
		return RentalReturnResponseDTO.builder()
				.rentalId(request.getRentalId())
				.success(true)
				.errorMessage(null)
				.build();
	}

	public Page<RentalHistoryResponseDTO> getRentalHistories(CustomUserDetails userDetails, Pageable pageable) {
		Page<Rental> rentalPage = rentalRepository.searchRentalHistories(userDetails.getUserId(), pageable);
		return rentalPage.map(RentalHistoryResponseDTO::from);
	}

	public RentalHistoryDetailResponseDTO getRentalHistoryDetail(CustomUserDetails userDetails, UUID rentalId) {
		Rental rental = rentalRepository.searchRentalHistoryDetail(userDetails.getUserId(), rentalId);
		return RentalHistoryDetailResponseDTO.from(rental);
	}
}