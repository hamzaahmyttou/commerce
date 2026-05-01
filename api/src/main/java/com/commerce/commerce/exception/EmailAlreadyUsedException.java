package com.commerce.commerce.exception;

public class EmailAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = -2062752808483825752L;

	public EmailAlreadyUsedException() {
		super("Email already used!");
	}	
}
