package com.qtechgames.rollernetwork.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> resourceNotFoundException(ResourceNotFoundException ex) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ErrorDetails> genericException(GenericException ex) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(ConstraintViolationException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		Map<String, String> errors = new LinkedHashMap<>();

		ex.getConstraintViolations().forEach(violation -> {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		});

		body.put("timestamp", new Date());
		body.put("errors", errors);

		return ResponseEntity.badRequest().body(body);
	}

}
