package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.response.AssetResponseDTO;
import com.example.demo.entity.AssetEntity;
import com.example.demo.model.UnitStatus;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, UUID> {

	@Query("""
			SELECT new com.example.demo.dto.response.AssetResponseDTO(
				a.assetId,
				a.name,
				a.categoryEntity.categoryCode,
				a.categoryEntity.name,
				a.model,
				a.manufacturer,
				CASE
					WHEN COUNT(
						CASE
							WHEN u.status = :availableStatus AND u.isDeleted = FALSE
							THEN 1
						END
					) > 0
					THEN TRUE
					ELSE FALSE
				END,
				COUNT(u.unitId),
				COUNT(
					CASE
						WHEN u.status = :availableStatus AND u.isDeleted = FALSE
						THEN 1
					END
				)
			)
			FROM AssetEntity a
			LEFT JOIN a.assetUnitEntities u
			WHERE a.isDeleted = FALSE
			GROUP BY
				a.assetId,
				a.name,
				a.categoryEntity.categoryCode,
				a.categoryEntity.name,
				a.model,
				a.manufacturer
			""")
	List<AssetResponseDTO> findAllActiveWithAsset(
			@Param("availableStatus") UnitStatus availableStatus);

	@Query("""
			SELECT a
			FROM AssetEntity a
			JOIN a.categoryEntity c
			WHERE
				a.isDeleted = FALSE
				AND c.isDeleted = FALSE
				AND EXISTS (
					SELECT 1
					FROM AssetUnitEntity u
					WHERE
						u.assetEntity = a
						AND u.isDeleted = FALSE
				)
				AND (
				:kw IS NULL OR :kw = ''
					OR LOWER(a.name) LIKE LOWER(CONCAT('%', :kw, '%'))
					OR LOWER(a.model) LIKE LOWER(CONCAT('%', :kw, '%'))
					OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :kw, '%'))
				)
				AND (
					:cc IS NULL OR :cc = ''
					OR LOWER(c.categoryCode) LIKE LOWER(CONCAT('%', :cc, '%'))
				)
			""")
	Page<AssetEntity> searchAssets(
			@Param("kw") String keyword,
			@Param("cc") String categoryCode,
			Pageable pageable);

	boolean existsByNameAndIsDeletedFalse(String name);

}
