package com.example.demo.exception;

/**
 * ユーザー認証関連の例外クラス
 */
public class AuthException extends RuntimeException {
	private final ApiErrorStatus apiErrorStatus;

	public AuthException(ApiErrorStatus apiErrorStatus) {
		super(apiErrorStatus.getMessage());
		this.apiErrorStatus = apiErrorStatus;
	}

	public ApiErrorStatus getApiErrorStatus() {
	    return apiErrorStatus;
	}
}
