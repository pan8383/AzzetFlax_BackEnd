package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.response.RentalDetailResponseDTO;
import com.example.demo.entity.RentalEntity;
import com.example.demo.model.RentalStatus;

public interface RentalRepository extends JpaRepository<RentalEntity, UUID> {

	//	boolean existsByAssetUnitEntity_UnitIdAndStatusIn(UUID unitId, List<RentalStatus> assetUnitStatus);

	/**
	 * レンタル履歴の簡易的な情報をページャーで返します
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query("""
			SELECT DISTINCT r
			FROM RentalEntity r
			LEFT JOIN FETCH r.rentalUnits ru
			WHERE r.userEntity.userId = :userId
				AND r.isDeleted = false
				AND (:statuses IS NULL OR r.status IN :statuses)
			""")
	Page<RentalEntity> searchRentalListWithUnits(
			@Param("statuses") List<RentalStatus> statuses,
			@Param("userId") UUID userId,
			Pageable pageable);

	@Query("""
			SELECT DISTINCT r
			FROM RentalEntity r
			LEFT JOIN FETCH r.rentalUnits ru
			LEFT JOIN FETCH ru.assetUnitEntity au
			WHERE r IN :rentals
			""")
	List<RentalEntity> findWithUnits(
			@Param("rentals") List<RentalEntity> rentals);

	@Modifying
	@Query("""
			UPDATE RentalEntity u
			SET u.status = :status
			WHERE u.rentalId = :rentalId
			""")
	int updateStatus(
			@Param("rentalId") UUID rentalId,
			@Param("status") RentalStatus status);

	@Query("""
			SELECT new com.example.demo.dto.response.RentalDetailResponseDTO(
				a.assetId,
				a.name,
				c.categoryCode,
				c.name,
				a.model,
				a.manufacturer,
				l.locationCode,
				l.name,
				au.unitId,
				au.serialNumber,
				au.status,
				au.purchaseDate,
				au.purchasePrice,
				au.remarks,
				ru.rentalUnitId,
				ru.rentalUnitStatus,
				ru.rentedAt,
				ru.returnedAt
			)
			FROM RentalUnitEntity ru
			JOIN ru.assetUnitEntity au
			JOIN au.assetEntity a
			JOIN a.categoryEntity c
			JOIN au.locationEntity l
			JOIN ru.rentalEntity r
			JOIN r.userEntity u
			WHERE r.rentalId = :rentalId
			""")
	List<RentalDetailResponseDTO> findRentalDetails(@Param("rentalId") UUID rentalId);

}
