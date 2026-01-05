package com.flux.azzet.dto.response;

import com.flux.azzet.exception.ApiErrorStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
	private boolean success;
	private T data;
	private ErrorResponseDTO error;
	private Object errorDetails;

	// 成功レスポンス
	public static <T> ApiResponseDTO<T> success(T data) {
		return new ApiResponseDTO<>(true, data, null, null);
	}

	// エラーレスポンス（details なし）
	public static <T> ApiResponseDTO<T> error(ApiErrorStatus status) {
		return new ApiResponseDTO<>(false, null,
				new ErrorResponseDTO(status.getMessage(), status.getHttpStatus()),
				null);
	}

	// エラーレスポンス（details あり）
	public static <T> ApiResponseDTO<T> error(ApiErrorStatus status, Object details) {
		return new ApiResponseDTO<>(false, null,
				new ErrorResponseDTO(status.getMessage(), status.getHttpStatus()),
				details);
	}
}
