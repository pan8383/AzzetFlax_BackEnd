package com.flux.azzet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flux.azzet.dto.request.RentalCreateRequestDTO;
import com.flux.azzet.dto.request.RentalReturnRequestDTO;
import com.flux.azzet.dto.request.RentalUnitRequestDTO;
import com.flux.azzet.dto.response.ApiResponseDTO;
import com.flux.azzet.dto.response.PageResponseDTO;
import com.flux.azzet.dto.response.RentalAssetsListResponseDTO;
import com.flux.azzet.dto.response.RentalCreateAssetResponseDTO;
import com.flux.azzet.dto.response.RentalCreateResponseDTO;
import com.flux.azzet.dto.response.RentalCreateUnitResponseDTO;
import com.flux.azzet.dto.response.RentalDetailResponseDTO;
import com.flux.azzet.dto.response.RentalReturnResponseDTO;
import com.flux.azzet.dto.response.RentalUnitResponseDTO;
import com.flux.azzet.entity.AssetUnitEntity;
import com.flux.azzet.entity.RentalEntity;
import com.flux.azzet.entity.RentalUnitEntity;
import com.flux.azzet.entity.UserEntity;
import com.flux.azzet.exception.ApiErrorStatus;
import com.flux.azzet.exception.RentalException;
import com.flux.azzet.exception.UserException;
import com.flux.azzet.model.RentalStatus;
import com.flux.azzet.model.RentalUnitStatus;
import com.flux.azzet.model.UnitStatus;
import com.flux.azzet.repository.AssetUnitRepository;
import com.flux.azzet.repository.RentalRepository;
import com.flux.azzet.repository.RentalUnitRepository;
import com.flux.azzet.repository.UserRepository;
import com.flux.azzet.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RentalService {

	private final UserRepository userRepository;
	private final AssetUnitRepository assetUnitRepository;
	private final RentalRepository rentalRepository;
	private final RentalUnitRepository rentalUnitRepository;

	/**
	 * 指定されたステータスのレンタルアセット一覧を取得する
	 * @param statuses
	 * @param userDetails
	 * @param pageable
	 * @return
	 */
	public PageResponseDTO<RentalAssetsListResponseDTO> getRentalAssetList(
			List<RentalStatus> statuses,
			CustomUserDetails userDetails,
			Pageable pageable) {

		Page<RentalEntity> page = rentalRepository
				.searchRentalListWithUnits(statuses, userDetails.getUserId(), pageable);

		List<RentalAssetsListResponseDTO> content = page.getContent().stream()
				.map(rental -> new RentalAssetsListResponseDTO(
						rental.getRentalId(),
						rental.getUserEntity().getUserId(),
						rental.getRentalDate(),
						rental.getExpectedReturnDate(),
						rental.getActualReturnDate(),
						rental.getStatus(),
						rental.getRemarks(),
						rental.getRentalUnits().stream()
								.map(ru -> new RentalUnitResponseDTO(
										ru.getRentalUnitId(),
										ru.getAssetUnitEntity().getUnitId(),
										ru.getRentalUnitStatus(),
										ru.getRentedAt(),
										ru.getReturnedAt()))
								.toList()))
				.toList();

		return PageResponseDTO.<RentalAssetsListResponseDTO> builder()
				.content(content)
				.pageNumber(page.getNumber())
				.pageSize(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.build();
	}

	/**
	 * 指定されたレンタルIDに紐づくレンタル詳細情報の一覧を取得する
	 * @param rentalId
	 * @return
	 */
	public ApiResponseDTO<List<RentalDetailResponseDTO>> getRentalListDetails(UUID rentalId) {
		List<RentalDetailResponseDTO> rentalDetails = rentalRepository.findRentalDetails(rentalId);
		return ApiResponseDTO.success(rentalDetails);
	}

	/**
	 * 新しいレンタルを作成し、レンタルユニットを割り当てる
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public RentalCreateResponseDTO create(
			RentalCreateRequestDTO request, CustomUserDetails userDetails) {

		// トータルカウンタ
		int totalCount = 0;

		// 処理済みカウンタ
		int processedCount = 0;

		if (request.getAssets() == null || request.getAssets().isEmpty()) {
			throw new RentalException(ApiErrorStatus.RENTAL_UNIT_NOT_FOUND);
		}

		// アセットごとの結果リストを作成
		List<RentalCreateAssetResponseDTO> assetResponses = new ArrayList<>();

		// ユーザー取得
		UserEntity user = userRepository.findById(userDetails.getUserId())
				.orElseThrow(() -> new UserException(ApiErrorStatus.USER_NOT_FOUND));

		// レンタルIDを先に発行
		RentalEntity rentalEntity = RentalEntity.builder()
				.userEntity(user) // レンタルしたユーザー
				.rentalDate(LocalDate.now()) // レンタル日
				.expectedReturnDate(request.getExpectedReturnDate()) // 返却予定日
				.status(RentalStatus.PROCESSING) // 処理中
				.remarks(
						// 空文字の場合 null に正規化
						request.getRemarks() == null || request.getRemarks().trim().isEmpty()
								? null
								: request.getRemarks())
				.build();

		rentalRepository.saveAndFlush(rentalEntity);

		// レンタルするアセットの数だけループ
		for (RentalUnitRequestDTO asset : request.getAssets()) {
			totalCount += asset.getQuantity();

			// ユニットごとの結果リストを作成
			List<RentalCreateUnitResponseDTO> unitResponses = new ArrayList<>();

			// 利用可能なユニットを Quantity 分 ロックして取得
			List<AssetUnitEntity> units = assetUnitRepository.findAvailableUnitsForUpdate(
					asset.getAssetId(),
					UnitStatus.AVAILABLE,
					PageRequest.of(0, asset.getQuantity()));

			// 取得したユニットからIDを取り出す
			List<UUID> unitIds = units.stream()
					.map(AssetUnitEntity::getUnitId)
					.toList();

			// まとめてステータス更新
			assetUnitRepository.updateStatusIn(
					unitIds,
					UnitStatus.IN_USE);

			for (AssetUnitEntity unit : units) {

				// レンタルユニットIDを発行
				RentalUnitEntity rentalUnitEntity = RentalUnitEntity.builder()
						.rentalEntity(rentalEntity)
						.assetUnitEntity(unit)
						.rentalUnitStatus(RentalUnitStatus.RENTING)
						.rentedAt(LocalDateTime.now())
						.build();
				rentalUnitRepository.save(rentalUnitEntity);

				// ユニットごとのレスポンス結果を追加
				unitResponses.add(RentalCreateUnitResponseDTO.success(unit));

				// 処理済みカウンタ
				processedCount++;
			}

			// 在庫不足の場合
			if (units.size() < asset.getQuantity()) {

				// 借りられなかったユニット数
				int unavailableUnitCount = asset.getQuantity() - units.size();

				// ユニットごとのレスポンス結果を追加
				for (int i = 0; i < unavailableUnitCount; i++) {
					unitResponses.add(RentalCreateUnitResponseDTO.failure(asset.getAssetId(), "在庫不足のためレンタルに失敗"));
				}
			}
			// アセットごとのレスポンス結果を追加
			assetResponses.add(RentalCreateAssetResponseDTO.from(asset, unitResponses));
		}

		// @Transactional 内では、エンティティは managed 状態 なので setter で変更するだけで自動的に flush される
		// 全権失敗の場合 レンタルのステータスを 処理中 -> 全件失敗 に更新
		if (processedCount == 0) {
			rentalEntity.setStatus(RentalStatus.FAILED);
		} else if (processedCount < totalCount) {
			// レンタルのステータスを 処理中 -> 一部成功・一部失敗 に更新
			rentalEntity.setStatus(RentalStatus.PARTIAL);
		} else if (processedCount == totalCount) {
			// レンタルのステータスを 処理中 -> レンタル中 に更新
			rentalEntity.setStatus(RentalStatus.RENTED);
		}
		return RentalCreateResponseDTO.from(rentalEntity, assetResponses);
	}

	/**
	 * 指定されたレンタルユニットを返却処理する
	 * @param requests
	 * @param userDetails
	 * @return
	 */
	@Transactional
	public List<RentalReturnResponseDTO> returnUnits(
			List<RentalReturnRequestDTO> requests,
			CustomUserDetails userDetails) {

		// レスポンス用結果リストを作成
		List<RentalReturnResponseDTO> responses = new ArrayList<>();

		for (RentalReturnRequestDTO request : requests) {

			// リクエストからレンタルユニットを取得
			List<RentalUnitEntity> rentalUnitEntities = rentalUnitRepository.findByIds(request.getRentalUnitIds());

			// 返却するユニットの数とレンタルしたユニットの数が一致しない場合
			if (rentalUnitEntities.size() < request.getRentalUnitIds().size()) {
				//TODO 例外処理
			}

			// レンタルリストから ユニットID を抽出
			List<UUID> assetUnitIds = rentalUnitEntities.stream()
					.map(RentalUnitEntity::getAssetUnitEntity)
					.map(AssetUnitEntity::getUnitId)
					.toList();

			// AssetUnitEntity のステータスを 利用可能 に更新
			assetUnitRepository.updateStatusIn(assetUnitIds, UnitStatus.AVAILABLE);

			// レンタルリストから レンタルユニットID を抽出
			List<UUID> rentalUnitIds = rentalUnitEntities.stream()
					.map(RentalUnitEntity::getRentalUnitId)
					.toList();

			// RentalUnitEntity のステータスを 返却済み に更新
			rentalUnitRepository.updateStatusIn(rentalUnitIds, RentalUnitStatus.RETURNED);

			rentalUnitEntities = rentalUnitRepository.findByRentalId(request.getRentalId());

			// レンタル中をカウント
			int rentingCount = rentalUnitRepository.countByRentalEntity_RentalIdAndRentalUnitStatus(
					request.getRentalId(),
					RentalUnitStatus.RENTING);

			// レンタルしたユニットの総数
			int totalCount = rentalUnitRepository.countByRentalEntity_RentalId(request.getRentalId());

			RentalStatus status = rentingCount == 0 ? RentalStatus.RETURNED : RentalStatus.PARTIAL;

			// レンタル中が1件もなければ → 全返却
			if (status == RentalStatus.RETURNED) {
				rentalRepository.updateStatus(request.getRentalId(), RentalStatus.RETURNED);
			}

			responses.add(new RentalReturnResponseDTO(
					request.getRentalId(),
					status,
					(int) (totalCount - rentingCount),
					(int) totalCount));
		}
		return responses;
	}
}