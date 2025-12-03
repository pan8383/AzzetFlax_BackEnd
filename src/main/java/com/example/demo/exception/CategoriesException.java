package com.example.demo.exception;

/**
 * カテゴリー関連の例外クラス
 */
public class CategoriesException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public CategoriesException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
	    return apiErrorStatus;
	}
}
