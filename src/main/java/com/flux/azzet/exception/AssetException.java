package com.flux.azzet.exception;

/**
 * アセット関連の例外クラス
 */
public class AssetException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public AssetException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
		return apiErrorStatus;
	}
}
