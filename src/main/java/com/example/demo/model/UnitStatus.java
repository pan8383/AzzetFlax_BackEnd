package com.example.demo.model;

public enum AssetUnitStatus {
	//@formatter:off
	AVAILABLE	("利用可能"),
	IN_USE		("使用中"),
	MAINTENANCE	("メンテ中"),
	BROKEN		("故障"),
	DISPOSED	("廃棄済み");
	//@formatter:on

	private final String description;

	private AssetUnitStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
