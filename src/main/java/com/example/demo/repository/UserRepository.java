package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmailAndIsDeletedFalse(String email);
    
    Optional<User> findByEmailAndIsDeletedFalse(String email);
}