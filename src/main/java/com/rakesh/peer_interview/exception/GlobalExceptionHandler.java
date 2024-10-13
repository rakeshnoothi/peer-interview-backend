package com.rakesh.peer_interview.exception;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rakesh.peer_interview.httpResponseUtil.CustomResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException exception){
		return new ResponseEntity<String>("client provided credentials are invalid", HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(InterruptedException.class)
	public ResponseEntity<CustomResponse> handleInterruptedException(InterruptedException e){
		CustomResponse customResponse = CustomResponse.getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"something went wrong server side" , "");
		return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(customResponse.getStatus()));
	}
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<CustomResponse> handleInterruptedException(SQLIntegrityConstraintViolationException e){
		CustomResponse customResponse = CustomResponse.getResponse(HttpStatus.CONFLICT.value(),"User already exists with the provided username" , "");
		return new ResponseEntity<CustomResponse>(customResponse, HttpStatusCode.valueOf(customResponse.getStatus()));
	}
}
