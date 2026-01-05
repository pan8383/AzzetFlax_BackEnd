package com.flux.azzet.entity;

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

import com.flux.azzet.model.RentalUnitStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rental_units")
public class RentalUnitEntity {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID rentalUnitId;

	@ManyToOne
	@JoinColumn(name = "rental_id")
	private RentalEntity rentalEntity;

	@ManyToOne
	@JoinColumn(name = "unit_id")
	private AssetUnitEntity assetUnitEntity;

	@Enumerated(EnumType.STRING)
	@Column(name = "rental_status")
	private RentalUnitStatus rentalUnitStatus;

	@Column(name = "rented_at")
	private LocalDateTime rentedAt;

	@Column(name = "returned_at", nullable = true)
	private LocalDateTime returnedAt;
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