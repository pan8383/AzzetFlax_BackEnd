package com.flux.azzet.exception;

import lombok.Getter;

/**
 * ユーザー関連の例外クラス
 */
@Getter
public class UserException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public UserException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
		return apiErrorStatus;
	}
}
