package com.worldpay.assignment.merchantoffer.error;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RestApiError {

	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String type;
	private String message;
	private List<String> errors;

	public RestApiError() {
		timestamp = LocalDateTime.now();
	}

	public RestApiError(HttpStatus status) {
		this();
		this.status = status;
	}

	public RestApiError(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.type = "Unexpected error";
		this.message = ex.getMessage();
	}

	public RestApiError(HttpStatus status, String type) {
		this();
		this.status = status;
		this.type = type;
	}

	public RestApiError(HttpStatus status, String type, String message) {
		this();
		this.status = status;
		this.type = type;
		this.message = message;
	}
	
	public RestApiError(HttpStatus status, String type, String message, List<String> errors) {
		this();
		this.status = status;
		this.type = type;
		this.message = message;
		this.errors = errors;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}