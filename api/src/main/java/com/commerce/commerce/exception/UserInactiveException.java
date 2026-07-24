package com.commerce.commerce.exception;

public class UserInactiveException extends RuntimeException {

	public UserInactiveException() {
		super("User inactive!");
	}
}
