package com.commerce.commerce.exception;

public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = -2062752808483825752L;

	public InvalidCredentialsException() {
		super("Invalid credentials!");
	}
}
