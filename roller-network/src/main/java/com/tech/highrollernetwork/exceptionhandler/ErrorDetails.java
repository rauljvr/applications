package com.tech.highrollernetwork.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {

	private Date timestamp;
	private int statusCode;
	private String message;
}
