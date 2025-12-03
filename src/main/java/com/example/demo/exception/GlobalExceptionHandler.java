package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.dto.response.ApiResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * categories関連のハンドリングメソッド
	 * @param ex CategoriesException
	 * @return クライアントへエラーレスポンスを返します
	 */
	@ExceptionHandler(CategoriesException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleCategoriesException(CategoriesException ex) {
		ApiErrorStatus error = ex.getApiErrorStatus();
	    return ResponseEntity.status(error.getHttpStatus())
	                         .body(error.toResponse());
	}
	
	/**
	 * rentals関連のハンドリングメソッド
	 * @param ex RentalsException
	 * @return
	 */
	@ExceptionHandler(RentalException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleRentalsException(RentalException ex) {
	    ApiErrorStatus error = ex.getApiErrorStatus();
	    return ResponseEntity.status(error.getHttpStatus())
	                         .body(error.toResponse());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleBadRequest(IllegalArgumentException ex) {
		ApiErrorStatus error = ApiErrorStatus.BAD_REQUEST;
		return ResponseEntity.status(error.getHttpStatus())
				.body(error.toResponse());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleGeneric(Exception ex) {
		ApiErrorStatus error = ApiErrorStatus.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(error.getHttpStatus())
				.body(error.toResponse());
	}
}
