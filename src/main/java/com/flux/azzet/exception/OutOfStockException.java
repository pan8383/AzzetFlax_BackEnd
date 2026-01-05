package com.flux.azzet.exception;

import java.util.UUID;

/**
 * アセット関連の例外クラス
 */
public class OutOfStockException extends RuntimeException {
	private final UUID assetId;
	private final int requested;
	private final int available;

	public OutOfStockException(UUID assetId, int requested, int available) {
		super(String.format(
				"在庫不足: assetId=%s, requested=%d, available=%d",
				assetId, requested, available));
		this.assetId = assetId;
		this.requested = requested;
		this.available = available;
	}
}
