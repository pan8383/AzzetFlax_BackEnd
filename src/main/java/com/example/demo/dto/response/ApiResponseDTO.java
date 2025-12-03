package com.example.demo.dto.response;

import org.springframework.http.HttpStatus;

import com.example.demo.exception.ApiErrorStatus;

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

    // 成功レスポンス
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, data, null);
    }

    // エラーレスポンス（HTTPステータス込み）
    public static <T> ApiResponseDTO<T> error(String code, String message, HttpStatus status) {
        return new ApiResponseDTO<>(false, null, new ErrorResponseDTO(code, message, status));
    }

    // Enum から直接生成
    public static <T> ApiResponseDTO<T> error(ApiErrorStatus apiErrorStatus) {
        return new ApiResponseDTO<>(false, null,
            new ErrorResponseDTO(apiErrorStatus.getCode(), apiErrorStatus.getMessage(), apiErrorStatus.getHttpStatus()));
    }
}
