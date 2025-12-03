package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
	@Query("""
			SELECT c
			FROM Category c
			WHERE c.isDeleted = false
			ORDER BY c.sortOrder ASC
			""")
	List<Category> findAllActive();

	Category findByNameAndIsDeletedTrue(String name);
}
