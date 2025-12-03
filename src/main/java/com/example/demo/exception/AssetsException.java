package com.example.demo.exception;

/**
 * アセット関連の例外クラス
 */
public class AssetsException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public AssetsException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
		return apiErrorStatus;
	}
}
