package com.flux.azzet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flux.azzet.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {
	@Query("""
			SELECT l
			FROM LocationEntity l
			WHERE l.isDeleted = false
			ORDER BY l.sortOrder ASC
			""")
	List<LocationEntity> findAllActive();

	Optional<LocationEntity> findByName(String name);
}
