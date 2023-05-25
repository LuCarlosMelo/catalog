package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = insertStandardError(status, "Resource not found", e, request);
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> dataBase(DataBaseException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError standardError = insertStandardError(status, "Database excpetion", e, request);
		return ResponseEntity.status(status).body(standardError);

	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError validationError = insertStandardError(status, "Validation excpetion", e, request);

		for(FieldError f: e.getBindingResult().getFieldErrors()){
			validationError.addError(f.getField(), f.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(validationError);
	}

	private ValidationError insertStandardError(HttpStatus status, String error, Exception e,
			HttpServletRequest request) {
		ValidationError validationError = new ValidationError();
		validationError.setTimestamp(Instant.now());
		validationError.setStatus(status.value());
		validationError.setError(error);
		validationError.setMessage(e.getMessage());
		validationError.setPath(request.getRequestURI());
		return validationError;
	}
}
