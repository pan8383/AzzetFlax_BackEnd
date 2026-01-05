package com.flux.azzet.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.flux.azzet.model.RentalStatus;

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
public class RentalEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@Column(name = "id")
	private UUID rentalId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity userEntity;

	@Column(name = "rental_date")
	private LocalDate rentalDate;

	@Column(name = "expected_return_date")
	private LocalDate expectedReturnDate;

	@Column(name = "actual_return_date", nullable = true)
	private LocalDate actualReturnDate;

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

	@OneToMany(mappedBy = "rentalEntity", fetch = FetchType.LAZY)
	private List<RentalUnitEntity> rentalUnits = new ArrayList<>();
}