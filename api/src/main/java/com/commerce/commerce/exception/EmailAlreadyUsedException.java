package com.commerce.commerce.exception;

public class EmailAlreadyUsedException extends RuntimeException {

	public EmailAlreadyUsedException() {
		super("Email already used!");
	}	
}
