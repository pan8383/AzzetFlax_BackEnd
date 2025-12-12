package com.example.demo.exception;

import org.springframework.http.HttpStatus;

import com.example.demo.dto.response.ApiResponseDTO;

import lombok.Getter;

@Getter
public enum ApiErrorStatus {

	//@formatter:off
	/**
	 * 認証関連
	 */
	INVALID_CREDENTIALS(
		"メールアドレスまたはパスワードが正しくありません",
		HttpStatus.BAD_REQUEST
	),
	AUTHENTICATION_REQUIRED(
		"認証が必要です",
		HttpStatus.UNAUTHORIZED
	),
	

	/**
	 * ユーザー関連
	 */
	USERNAME_ALREADY_EXISTS(
		"このユーザー名は既に使用されています",
		HttpStatus.CONFLICT
	),
	EMAIL_ALREADY_EXISTS(
		"このメールアドレスは既に使用されています",
		HttpStatus.CONFLICT
	),
	USER_NOT_FOUND(
		"ユーザーが見つかりません",
		HttpStatus.NOT_FOUND
	),
	SERIAL_NUMBER_DUPLICATED(
		"シリアル番号が重複しています",
		HttpStatus.BAD_REQUEST
	),


	/**
	 * アセット関連
	 */
	ASSET_NOT_FOUND(
		"アセットが見つかりません",
		HttpStatus.NOT_FOUND
	),
	ASSET_STATUS_UPDATE_FAILED(
		"アセットの状態更新に失敗しました",
		HttpStatus.CONFLICT
	),
	ASSET_UNIT_NOT_AVAILABLE(
		"利用可能なアセットユニットがありません",
		HttpStatus.NOT_FOUND
	),


	/**
	 * カテゴリー関連
	 */
	CATEGORY_CODE_ALREADY_EXISTS(
		"このカテゴリーコードは既に使用されています",
		HttpStatus.CONFLICT
	),
	CATEGORY_NAME_ALREADY_EXISTS(
		"このカテゴリー名は既に使用されています",
		HttpStatus.CONFLICT
	),
	CATEGORY_NOT_FOUND(
		"カテゴリーが見つかりません",
		HttpStatus.NOT_FOUND
	),
	CATEGORY_CREATE_FAILED(
		"カテゴリーの登録に失敗しました",
		HttpStatus.INTERNAL_SERVER_ERROR
	),
	CATEGORY_DELETE_FAILED(
		"カテゴリーの削除に失敗しました",
		HttpStatus.INTERNAL_SERVER_ERROR
	),
	
	/**
	 * ロケーション関連
	 */
	LOCATION_CODE_ALREADY_EXISTS(
		"このロケーションコードは既に使用されています",
		HttpStatus.CONFLICT
	),
	LOCATION_NAME_ALREADY_EXISTS(
		"このロケーション名は既に使用されています",
		HttpStatus.CONFLICT
	),
	LOCATION_NOT_FOUND(
		"ロケーションが見つかりません",
		HttpStatus.NOT_FOUND
	),
	LOCATION_CREATE_FAILED(
		"ロケーションの登録に失敗しました",
		HttpStatus.INTERNAL_SERVER_ERROR
	),
	LOCATION_DELETE_FAILED(
		"ロケーションの削除に失敗しました",
		HttpStatus.INTERNAL_SERVER_ERROR
	),


	/**
	 * レンタル関連
	 */
	RENTAL_ALREADY_EXISTS(
		"既にレンタルされています",
		HttpStatus.CONFLICT
	),
	RENTAL_UNIT_NOT_FOUND(
		"レンタル可能なユニットがありません",
		HttpStatus.NOT_FOUND
	),
	RENTAL_COMMIT_FAILED(
		"レンタル処理に失敗しました",
		HttpStatus.CONFLICT
	),
	RENTAL_RETURN_FAILED(
		"返却処理に失敗しました",
		HttpStatus.CONFLICT
	),


	/**
	 * 共通エラー
	 */
	RESOURCE_CONFLICT(
		"リソースが既に存在します",
		HttpStatus.CONFLICT
	),
	INVALID_REQUEST(
		"リクエストが正しくありません",
		HttpStatus.BAD_REQUEST
	),
	INTERNAL_SERVER_ERROR(
		"サーバ内部でエラーが発生しました",
		HttpStatus.INTERNAL_SERVER_ERROR
	);
	//@formatter:on

	private final String message;
	private final HttpStatus httpStatus;

	ApiErrorStatus(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	/** Enum から直接 ApiResponseDTO を生成 */
	public <T> ApiResponseDTO<T> toResponse() {
		return ApiResponseDTO.error(this);
	}
}
