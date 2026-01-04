package com.example.demo.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RentalCreateRequestDTO {

	@NotNull(message = "返却予定日は必須です")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expectedReturnDate;

	private String remarks;

	private List<RentalUnitRequestDTO> assets;
}
