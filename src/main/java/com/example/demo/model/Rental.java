package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rentals")
public class Rental {
	
	@Id
	@GeneratedValue
	@Column(name = "rental_id")
	private UUID rentalId;
	
	@ManyToOne
	@JoinColumn(name = "asset_id")
	private Asset asset;
	
	@ManyToOne
	@JoinColumn(name = "unit_id")
	private AssetUnit assetUnit;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "due")
	private Date due;

	@Column(name = "return_at", nullable = true)
	private LocalDateTime returnAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private RentalStatus status;

	@Column(name = "remarks", nullable = true)
	private String remarks;

	@Builder.Default
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}