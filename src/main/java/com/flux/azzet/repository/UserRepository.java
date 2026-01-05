package com.flux.azzet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flux.azzet.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	boolean existsByEmailAndIsDeletedFalse(String email);

	Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);

	@Query("""
			SELECT u
			FROM UserEntity u
			WHERE u.isDeleted = FALSE
				AND (:kw IS NULL OR :kw = ''
					OR LOWER(u.name) LIKE LOWER(CONCAT('%', :kw, '%'))
					OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%'))
				)
			""")
	Page<UserEntity> searchUsers(@Param("kw") String keyword, Pageable pageable);
}