package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.response.AssetUnitDetailResponseDTO;
import com.example.demo.entity.AssetUnitEntity;
import com.example.demo.model.UnitStatus;

public interface AssetUnitRepository extends JpaRepository<AssetUnitEntity, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
			    SELECT u
			    FROM AssetUnitEntity u
			    WHERE u.assetEntity.assetId = :assetId
			      AND u.status = :status
			      AND u.isDeleted = false
			    ORDER BY u.unitId ASC
			""")
	List<AssetUnitEntity> findAvailableUnitsForUpdate(
			@Param("assetId") UUID assetId,
			@Param("status") UnitStatus status,
			Pageable pageable);

	// 利用可能なアセットがあるか検索する
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<AssetUnitEntity> findFirstByAssetEntityAssetIdAndStatusAndIsDeletedFalseOrderByUnitIdAsc(
			UUID assetId,
			UnitStatus status);

	@Modifying
	@Query("""
			UPDATE AssetUnitEntity u
			SET u.status = :status
			WHERE u.unitId IN :unitIds
			""")
	int updateStatusIn(
			@Param("unitIds") List<UUID> unitIds,
			@Param("status") UnitStatus status);

	@Query("""
			    SELECT new com.example.demo.dto.response.AssetUnitDetailResponseDTO(
			        u.unitId,
			        u.serialNumber,
			        u.status,
			        u.purchaseDate,
			        u.purchasePrice,
			        u.remarks,
			        u.locationEntity.locationCode,
			        u.locationEntity.name,
			        a.assetId,
			        a.name,
			        a.categoryEntity.categoryCode,
			        a.categoryEntity.name,
			        a.model,
			        a.manufacturer
			    )
			    FROM AssetUnitEntity u
			    LEFT JOIN u.assetEntity a
			    WHERE a.isDeleted = FALSE
			      AND u.isDeleted = FALSE
			      AND a.assetId = :assetId
			""")
	List<AssetUnitDetailResponseDTO> findAllActiveWithAssetUnitByAssetId(@Param("assetId") UUID assetId);
}
