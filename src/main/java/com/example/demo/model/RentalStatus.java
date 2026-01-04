package com.example.demo.model;

public enum RentalStatus {
	PROCESSING, // 処理中（IDの払い出し）
	RENTED, // 全ユニット貸出成功
	PARTIAL, // 一部成功・一部失敗
	RETURNED, // 全返却済み
	LOST, // 紛失
	BROKEN, // 破損
	FAILED; // 全件失敗（在庫不足など）
}
