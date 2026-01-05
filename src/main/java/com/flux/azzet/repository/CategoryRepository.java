package com.flux.azzet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flux.azzet.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
	@Query("""
			SELECT c
			FROM CategoryEntity c
			WHERE c.isDeleted = false
			ORDER BY c.sortOrder ASC
			""")
	List<CategoryEntity> findAllActive();

	Optional<CategoryEntity> findByName(String name);
}
