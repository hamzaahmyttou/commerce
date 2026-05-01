package com.commerce.commerce.exception;

public class UserInactiveException extends RuntimeException {

	private static final long serialVersionUID = -2062752808483825752L;

	public UserInactiveException() {
		super("User inactive!");
	}
}
