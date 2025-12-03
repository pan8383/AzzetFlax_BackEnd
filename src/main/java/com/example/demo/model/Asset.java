package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assets")
public class Asset {
	@Id
	@Column(name = "asset_id")
	private UUID assetId;

	@Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "category_cd")
	private Category category;

	@Column(name = "model")
	private String model;

	@Column(name = "manufacturer")
	private String manufacturer;

	@Builder.Default
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@ToString.Exclude
	@Builder.Default
	@OneToMany(mappedBy = "asset")
	private List<AssetUnit> assetUnits = new ArrayList<>();
}