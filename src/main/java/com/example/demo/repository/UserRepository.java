package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	boolean existsByEmailAndIsDeletedFalse(String email);
    
    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);
}