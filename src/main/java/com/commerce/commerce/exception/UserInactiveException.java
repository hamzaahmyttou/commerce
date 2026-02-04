package com.commerce.commerce.exception;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class UserInactiveException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2062752808483825752L;
}
