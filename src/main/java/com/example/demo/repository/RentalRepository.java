package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Rental;
import com.example.demo.model.RentalStatus;

public interface RentalRepository extends JpaRepository<Rental, UUID> {

	boolean existsByAssetUnitUnitIdAndStatusIn(UUID unitId, List<RentalStatus> assetUnitStatus);

	/**
	 * レンタル履歴の簡易的な情報をページャーで返します
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query("""
			SELECT r
			FROM Rental r
			WHERE
				r.user.id = :userId
				AND r.isDeleted = false
			""")
	Page<Rental> searchRentalHistories(
			@Param("userId") UUID userId,
			Pageable pageable);

	/**
	 * レンタル履歴の詳細情報を１件だけ返します
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query("""
			SELECT r
			FROM Rental r
			WHERE
				r.user.id = :userId
				AND r.rentalId = :rentalId
				AND r.isDeleted = false
			""")
	Rental searchRentalHistoryDetail(
			@Param("userId") UUID userId,
			@Param("rentalId") UUID rentalId);

	/**
	 * 
	 * @param userId
	 * @param rentalId
	 * @param status
	 * @return
	 */
	@Modifying
	@Query("""
			    UPDATE Rental r
			    SET r.status = :status,
			        r.returnAt = :returnAt
			    WHERE r.user.userId = :userId
			      AND r.rentalId = :rentalId
			""")
	int updateStatus(
			@Param("userId") UUID userId,
			@Param("rentalId") UUID rentalId,
			@Param("returnAt") LocalDateTime returnAt,
			@Param("status") RentalStatus status);

}
