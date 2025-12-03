package com.example.demo.dto.request;

import java.sql.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalRegisterRequestDTO {
	@NonNull
	private UUID assetId;
	
	@NonNull
	private Integer quantity;
	
	@NonNull
	private Date due;
	
	@NonNull
	private String remarks;
}
