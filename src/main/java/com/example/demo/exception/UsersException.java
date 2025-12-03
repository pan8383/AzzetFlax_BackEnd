package com.example.demo.exception;

import lombok.Getter;

/**
 * ユーザー関連の例外クラス
 */
@Getter
public class UsersException extends RuntimeException {

	private final ApiErrorStatus apiErrorStatus;

	public UsersException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getCode());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
		return apiErrorStatus;
	}
}
