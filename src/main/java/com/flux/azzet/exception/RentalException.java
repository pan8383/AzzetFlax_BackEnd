package com.flux.azzet.exception;

/**
 * レンタル関連の例外クラス
 */
public class RentalException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public RentalException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
		return apiErrorStatus;
	}
}
