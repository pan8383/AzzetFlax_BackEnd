package com.flux.azzet.exception;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.flux.azzet.dto.response.ApiResponseDTO;
import com.flux.azzet.util.SerialNumberExtractor;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * バリデーション用の例外ハンドラ
	 * 単体 DTO の @Valid
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleValidationError(MethodArgumentNotValidException ex) {
		return ResponseEntity
				.status(ApiErrorStatus.INVALID_REQUEST.getHttpStatus())
				.body(ApiErrorStatus.INVALID_REQUEST.toResponse());
	}

	/**
	 * バリデーション用の例外ハンドラ
	 * List<DTO> の @Valid
	 */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleListValidationError(HandlerMethodValidationException ex) {
		return ResponseEntity
				.status(ApiErrorStatus.INVALID_REQUEST.getHttpStatus())
				.body(ApiErrorStatus.INVALID_REQUEST.toResponse());
	}

	/**
	 * JSONパースエラー（構文エラー・型不一致など）を処理する例外ハンドラ
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleJsonParseError(HttpMessageNotReadableException ex) {
		return ResponseEntity
				.status(ApiErrorStatus.INVALID_REQUEST.getHttpStatus())
				.body(ApiErrorStatus.INVALID_REQUEST.toResponse());
	}

	/**
	 * 認証関連のハンドリングメソッド
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleBadCredentials(BadCredentialsException ex) {
		return ResponseEntity
				.status(ApiErrorStatus.INVALID_CREDENTIALS.getHttpStatus())
				.body(ApiErrorStatus.INVALID_CREDENTIALS.toResponse());
	}

	/**
	 * 認証関連のハンドリングメソッド
	 */
	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleBadCredentials(AuthException ex) {
		ApiErrorStatus status = ex.getApiErrorStatus();
		return ResponseEntity
				.status(status.getHttpStatus())
				.body(status.toResponse());
	}

	/**
	 * 認証関連のハンドリングメソッド
	 * ユーザーが存在しない場合に発生
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleUserNotfound(UsernameNotFoundException ex) {
		return ResponseEntity
				.status(ApiErrorStatus.AUTHENTICATION_REQUIRED.getHttpStatus())
				.body(ApiErrorStatus.AUTHENTICATION_REQUIRED.toResponse());
	}

	/**
	 * ユーザー関連のハンドリングメソッド
	 */
	@ExceptionHandler(AssetException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleAssetException(AssetException ex) {
		ApiErrorStatus status = ex.getApiErrorStatus();
		return ResponseEntity
				.status(status.getHttpStatus())
				.body(status.toResponse());
	}

	/**
	 * ユーザー関連のハンドリングメソッド
	 */
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleUserException(UserException ex) {
		ApiErrorStatus status = ex.getApiErrorStatus();
		return ResponseEntity
				.status(status.getHttpStatus())
				.body(status.toResponse());
	}

	/**
	 * Category関連のハンドリングメソッド
	 * @param ex CategoriesException
	 * @return クライアントへエラーレスポンスを返します
	 */
	@ExceptionHandler(CategoryException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleCategoryException(CategoryException ex) {
		ApiErrorStatus status = ex.getApiErrorStatus();
		return ResponseEntity
				.status(status.getHttpStatus())
				.body(status.toResponse());
	}

	/**
	 * Location関連のハンドリングメソッド
	 * @param ex CategoriesException
	 * @return クライアントへエラーレスポンスを返します
	 */
	@ExceptionHandler(LocationException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleLocationException(LocationException ex) {
		ApiErrorStatus status = ex.getApiErrorStatus();
		return ResponseEntity
				.status(status.getHttpStatus())
				.body(status.toResponse());
	}

	/**
	 * rentals関連のハンドリングメソッド
	 * @param ex RentalsException
	 * @return
	 */
	@ExceptionHandler(RentalException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleRentalsException(RentalException ex) {
		ApiErrorStatus status = ex.getApiErrorStatus();
		return ResponseEntity
				.status(status.getHttpStatus())
				.body(status.toResponse());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity
				.status(ApiErrorStatus.INVALID_REQUEST.getHttpStatus())
				.body(ApiErrorStatus.INVALID_REQUEST.toResponse());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleGeneric(Exception ex) {
		return ResponseEntity
				.status(ApiErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
				.body(ApiErrorStatus.INTERNAL_SERVER_ERROR.toResponse());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponseDTO<Void>> handleDuplicateKey(DataIntegrityViolationException ex) {

		// どの制約に違反したかを判定
		String message = ex.getMostSpecificCause().getMessage();

		if (message != null && message.contains("uk_asset_units_serial")) {

			// 重複した serial_number を正規表現で抽出
			String duplicateSerial = SerialNumberExtractor.extract(message);

			return ResponseEntity
					.status(ApiErrorStatus.SERIAL_NUMBER_DUPLICATED.getHttpStatus())
					.body(ApiResponseDTO.error(
							ApiErrorStatus.SERIAL_NUMBER_DUPLICATED,
							Map.of("duplicateSerial", duplicateSerial)));
		}

		// その他の制約違反
		return ResponseEntity
				.status(ApiErrorStatus.INVALID_REQUEST.getHttpStatus())
				.body(ApiErrorStatus.INVALID_REQUEST.toResponse());
	}
}
