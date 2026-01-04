package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.RentalUnitEntity;
import com.example.demo.model.RentalUnitStatus;

public interface RentalUnitRepository extends JpaRepository<RentalUnitEntity, UUID> {
	@Query("""
			SELECT u
			FROM RentalUnitEntity u
			WHERE u.rentalUnitId IN :rentalUnitIds
			""")
	List<RentalUnitEntity> findByIds(
			@Param("rentalUnitIds") List<UUID> rentalUnitIds);

	// レンタルID と ステータス で検索。1件でもあれば True
	boolean existsByRentalEntity_RentalIdAndRentalUnitStatusNot(
			UUID rentalId,
			RentalUnitStatus rentalUnitStatus);

	@Query("""
			SELECT u
			FROM RentalUnitEntity u
			WHERE u.rentalEntity.rentalId = :rentalId
			""")
	List<RentalUnitEntity> findByRentalId(
			@Param("rentalId") UUID rentalId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("""
			UPDATE RentalUnitEntity r
			SET
				r.returnedAt = CURRENT_TIMESTAMP,
				r.rentalUnitStatus = :status
			WHERE r.rentalUnitId IN :unitIds
			""")
	int updateStatusIn(
			@Param("unitIds") List<UUID> unitIds,
			@Param("status") RentalUnitStatus status);

	int countByRentalEntity_RentalIdAndRentalUnitStatus(
			UUID rentalId,
			RentalUnitStatus rentalUnitStatus);

	int countByRentalEntity_RentalId(UUID rentalId);
}
