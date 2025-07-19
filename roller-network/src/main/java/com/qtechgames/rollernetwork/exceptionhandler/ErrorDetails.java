package com.qtechgames.rollernetwork.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {

	private Date timestamp;
	private String message;
}
