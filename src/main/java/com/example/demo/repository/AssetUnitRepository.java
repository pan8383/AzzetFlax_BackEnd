package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.AssetUnitEntity;
import com.example.demo.model.AssetUnitStatus;

public interface AssetUnitRepository extends JpaRepository<AssetUnitEntity, UUID> {

	// 利用可能なアセットがあるか検索する
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<AssetUnitEntity> findFirstByAssetEntityAssetIdAndStatusAndIsDeletedFalseOrderByUnitIdAsc(
			@Param("assetId") UUID assetId,
			@Param("status") AssetUnitStatus status);

	@Modifying
	@Transactional
	@Query("""
			UPDATE AssetUnitEntity a
			SET a.status = :status
			WHERE
				a.unitId = :uId
				AND a.isDeleted = false
			""")
	int updateStatus(
			@Param("uId") UUID unitId,
			@Param("status") AssetUnitStatus status);
}
