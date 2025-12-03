package com.example.demo.model;

public enum RentalStatus {
	//@formatter:off
    RENTED		("貸出中"),
    RETURNED	("返却済"),
    REQUESTED	("申請中"),
    CANCELED	("申請取消"),
    LOST		("紛失"),
    MAINTENANCE	("点検中");
	//@formatter:on

	private final String description;

	private RentalStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
