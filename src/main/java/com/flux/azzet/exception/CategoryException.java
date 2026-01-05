package com.flux.azzet.exception;

/**
 * カテゴリー関連の例外クラス
 */
public class CategoryException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public CategoryException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
	    return apiErrorStatus;
	}
}
