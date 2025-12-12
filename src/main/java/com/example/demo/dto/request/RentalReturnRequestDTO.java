package com.example.demo.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentalReturnRequestDTO {

    @NotNull
    private UUID rentalId;

    @NotNull
    private UUID unitId;
}

