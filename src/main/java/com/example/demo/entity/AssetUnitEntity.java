package com.example.demo.entity;

import java.math.BigDecimal;
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

import com.example.demo.model.AssetUnitStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asset_units")
public class AssetUnitEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID unitId;

	@ManyToOne
	@JoinColumn(name = "asset_id")
	private AssetEntity assetEntity;

	@Column(name = "serial_number")
	private String serialNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private AssetUnitStatus status;

	@ManyToOne
	@JoinColumn(name = "location_cd")
	private LocationEntity locationEntity;

	@Column(name = "purchase_date")
	private Date purchaseDate;

	@Column(name = "purchase_price")
	private BigDecimal purchasePrice;

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