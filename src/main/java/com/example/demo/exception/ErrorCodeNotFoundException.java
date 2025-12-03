package com.example.demo.exception;

public class ErrorCodeNotFoundException extends RuntimeException {

	public ErrorCodeNotFoundException() {
		super();
	}

	public ErrorCodeNotFoundException(String message) {
		super(message);
	}
}
