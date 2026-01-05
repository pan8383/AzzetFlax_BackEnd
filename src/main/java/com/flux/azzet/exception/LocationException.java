package com.flux.azzet.exception;

/**
 * ロケーション関連の例外クラス
 */
public class LocationException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public LocationException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
		return apiErrorStatus;
	}
}
