package com.flux.azzet.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.flux.azzet.model.UnitStatus;

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
	private UnitStatus status;

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
	private boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "assetUnitEntity")
	private List<RentalUnitEntity> rentalUnitEntities;

	/**
	 * 廃棄・削除を行う
	 * ステータスを DISPOSED にし、論理削除フラグを true にする
	 */
	public void dispose() {
		this.status = UnitStatus.DISPOSED;
		this.isDeleted = true;
	}

	/**
	 * 復帰させる（削除フラグを false にし、ステータスを AVAILABLE に戻す）
	 */
	public void restore() {
		this.status = UnitStatus.AVAILABLE;
		this.isDeleted = false;
	}
}