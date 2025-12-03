package com.example.demo.exception;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.example.demo.dto.response.ApiResponseDTO;

import lombok.Getter;

@Getter
public enum ApiErrorStatus {

	//@formatter:off
	
	/**
	 * user関連
	 */
    USERNAME_ALREADY_EXISTS(
    	"E001",
    	"このユーザー名は既に存在します",
    	HttpStatus.BAD_REQUEST
    ),
    EMAIL_ALREADY_EXISTS(
    	"E002",
    	"このメールアドレスは既に使用されています",
    	HttpStatus.BAD_REQUEST
    ),
    NOT_FOUND_USER(
    	"A002",
    	"ユーザーが見つかりません",
    	HttpStatus.NOT_FOUND
    ),
    UNAUTHORIZED(
    	"A003",
    	"メールアドレスまたはパスワードが正しくありません",
    	HttpStatus.UNAUTHORIZED
    ),
    UNAUTHORIZED_TOKEN(
    	"A004",
    	"トークンが正しくありません",
    	HttpStatus.UNAUTHORIZED
    ),
    
    /**
     * asset関連
     */
    ASSET_STATUS_UPDATE_FAILED(
	    "B001",
	    "アセットの状態更新に失敗しました",
	    HttpStatus.CONFLICT
	),
    ASSET_NOT_FOUND_ASSET_UNIT(
    	    "B002",
    	    "利用可能なアセットが存在しません",
    	    HttpStatus.NOT_FOUND
    	),
        
    
    /**
     * categories関連
     */
    CATEGORIES_ALREADY_EXISTS(
    	"C001",
    	"このカテゴリー名は既に存在します",
    	HttpStatus.CONFLICT
    ),
    CATEGORIES_NOT_FOUND(
    	"C002",
    	"カテゴリー名が見つかりません",
    	HttpStatus.NOT_FOUND
	),
    CATEGORIES_CREATE_FAILED(
    	"C003",
    	"カテゴリーの登録に失敗しました",
    	HttpStatus.INTERNAL_SERVER_ERROR
    ),
    CATEGORIES_DELETE_FAILED(
    	"C04",
    	"カテゴリーの削除時に失敗しました",
    	HttpStatus.INTERNAL_SERVER_ERROR
    ),
 
    /**
     * rentals関連
     */
    RENTAL_ALREADY_EXISTS(
		"D001",
		"既にレンタルされています",
		HttpStatus.CONFLICT
	),
    RENTAL_UNIT_NOT_FOUND(
		"D002",
		"レンタルできるユニットがありません",
		HttpStatus.NOT_FOUND
	),
    RENTAL_COMMIT_FAILED(
	    "D003",
	    "レンタルに失敗しました",
	    HttpStatus.CONFLICT
	),
    RENTAL_RETURN_FAILED(
	    "D004",
	    "レンタル返却に失敗しました",
	    HttpStatus.CONFLICT
	),

	/**
	 * サーバエラー関連
	 */
    CONFLICT(
    	"E900",
    	"リソースが既に存在します",
    	HttpStatus.CONFLICT
	),
    BAD_REQUEST(
    	"E901",
    	"リクエストが正しくありません",
    	HttpStatus.BAD_REQUEST
    ),
    INTERNAL_SERVER_ERROR(
    	"E999",
    	"サーバ内部でエラーが発生しました",
    	HttpStatus.INTERNAL_SERVER_ERROR
	);
	//@formatter:on

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	ApiErrorStatus(String code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	/** Enum から直接 ApiResponseDTO を生成 */
	public <T> ApiResponseDTO<T> toResponse() {
		return ApiResponseDTO.error(this.code, this.message, this.httpStatus);
	}

	/** 文字列コードから Enum を取得 */
	private static final Map<String, ApiErrorStatus> CODE_MAP = Arrays.stream(values())
			.collect(Collectors.toMap(ApiErrorStatus::getCode, e -> e));

	public static ApiErrorStatus fromCode(String code) throws ErrorCodeNotFoundException {
		ApiErrorStatus error = CODE_MAP.get(code);
		if (error == null)
			throw new ErrorCodeNotFoundException();
		return error;
	}
}
