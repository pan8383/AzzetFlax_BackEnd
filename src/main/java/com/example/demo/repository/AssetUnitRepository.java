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

import com.example.demo.model.AssetUnit;
import com.example.demo.model.AssetUnitStatus;

public interface AssetUnitRepository extends JpaRepository<AssetUnit, UUID> {

	//	@Query("""
	//			  SELECT a
	//			  FROM AssetStockSummaryView a
	//			  WHERE
	//			      (:kw IS NULL OR :kw = ''
	//			          OR LOWER(a.name) LIKE LOWER(CONCAT('%', :kw, '%'))
	//			          OR LOWER(a.model) LIKE LOWER(CONCAT('%', :kw, '%'))
	//			          OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :kw, '%')))
	//			      AND (:cn IS NULL OR :cn = '' OR LOWER(a.categoryName) LIKE LOWER(CONCAT('%', :cn, '%')))
	//			""")
	//	Page<AssetUnit> searchAssetUnits(
	//			@Param("kw") String keyword,
	//			@Param("cn") String categoryName,
	//			Pageable pageable);

	// 利用可能なアセットがあるか検索する
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<AssetUnit> findFirstByAssetAssetIdAndStatusAndIsDeletedFalseOrderByUnitIdAsc(
			@Param("assetId") UUID assetId,
			@Param("status") AssetUnitStatus status);

	@Modifying
	@Transactional
	@Query("""
			UPDATE AssetUnit a
			SET a.status = :status
			WHERE
				a.unitId = :uId
				AND a.isDeleted = false
			""")
	int updateStatus(
			@Param("uId") UUID unitId,
			@Param("status") AssetUnitStatus status);
}
