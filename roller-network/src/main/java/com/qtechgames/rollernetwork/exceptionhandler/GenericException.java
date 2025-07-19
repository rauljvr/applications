package com.qtechgames.rollernetwork.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GenericException(String message) {
		super(message);
	}
}