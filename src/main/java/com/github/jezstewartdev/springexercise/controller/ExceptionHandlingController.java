package com.github.jezstewartdev.springexercise.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.jezstewartdev.springexercise.exception.ValidationException;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?>  handleValidationException(ValidationException exception, HttpServletResponse response) {
		Map<String, String> result = new HashMap<>();
		result.put("error", exception.getMessage());
		return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);

	}

}
