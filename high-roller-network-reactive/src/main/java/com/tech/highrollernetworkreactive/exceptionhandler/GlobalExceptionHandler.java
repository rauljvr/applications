package com.tech.highrollernetworkreactive.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> resourceNotFoundException(ResourceNotFoundException ex) {
		log.info("Resource not found. {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> resourceNotFoundException(ResourceAlreadyExistsException ex) {
		log.info("Resource already exists. {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.CONFLICT.value(), ex.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorDetails> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		log.warn("Error found during validation: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Request parameter '" + ex.getName() + "' is invalid");

		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ErrorDetails> genericException(GenericException ex) {
		log.error("Unexpected error... ", ex);
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(ConstraintViolationException ex) {
		log.warn("Error(s) found during validation: {}", ex.getMessage());

		Map<String, Object> body = new LinkedHashMap<>();
		Map<String, String> errors = new LinkedHashMap<>();

		ex.getConstraintViolations().forEach(violation -> {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		});

		body.put("timestamp", new Date());
		body.put("statusCode", HttpStatus.BAD_REQUEST.value());
		body.put("errors", errors);

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleNotValidDtoErrors(MethodArgumentNotValidException ex) {
		log.warn("Error(s) found during validation: {}", ex.getMessage());

		Map<String, Object> body = new LinkedHashMap<>();
		Map<String, String> errors = new LinkedHashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage()));

		body.put("timestamp", new Date());
		body.put("statusCode", HttpStatus.BAD_REQUEST.value());
		body.put("errors", errors);

		return ResponseEntity.badRequest().body(body);
	}

}
