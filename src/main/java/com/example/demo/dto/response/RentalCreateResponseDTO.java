package com.example.demo.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RentalCreateResponseDTO {
    private UUID rentalId;
    private UUID assetId;
    private Boolean success;
    private String errorMessage;
}