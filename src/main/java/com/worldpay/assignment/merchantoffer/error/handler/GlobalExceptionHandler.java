package com.worldpay.assignment.merchantoffer.error.handler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.worldpay.assignment.merchantoffer.error.RestApiError;
import com.worldpay.assignment.merchantoffer.error.exception.*;
import com.worldpay.assignment.merchantoffer.error.message.ExceptionMessages;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private ResponseEntity<Object> buildResponseEntity(RestApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(OfferNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(OfferNotFoundException ex) {
		RestApiError apiError = new RestApiError(HttpStatus.NOT_FOUND);
		apiError.setType(OfferNotFoundException.class.getSimpleName());
		apiError.setMessage(ex.getMessage());
		apiError.setErrors(Arrays.asList(String.format(ex.getError(), ex.getOfferId())));
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(OfferPrimaryKeyViolationException.class)
	protected ResponseEntity<Object> handleOfferPrimaryKeyViolation(OfferPrimaryKeyViolationException ex) {
		RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST);
		apiError.setType(OfferPrimaryKeyViolationException.class.getSimpleName());
		apiError.setMessage(ex.getMessage());
		apiError.setErrors(Arrays.asList(String.format(ex.getError(), ex.getOfferId())));
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(IllegalOfferStatusException.class)
	protected ResponseEntity<Object> handleIllegalStatus(IllegalOfferStatusException ex) {
		RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST);
		apiError.setType(IllegalOfferStatusException.class.getSimpleName());
		apiError.setMessage(ex.getMessage());
		apiError.setErrors(Arrays.asList(String.format(ex.getError(), ex.getOfferId())));
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(InvalidOfferException.class)
	protected ResponseEntity<Object> handleInvalidOffer(InvalidOfferException ex) {
		RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST);
		apiError.setType(InvalidOfferException.class.getSimpleName());
		apiError.setMessage(ex.getMessage());
		apiError.setErrors(Arrays.asList(ex.getError()));
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MerchantNotFoundException.class)
	protected ResponseEntity<Object> handleMerchantNotFound(MerchantNotFoundException ex) {
		RestApiError apiError = new RestApiError(HttpStatus.NOT_FOUND);
		apiError.setType(MerchantNotFoundException.class.getSimpleName());
		apiError.setMessage(ex.getMessage());
		apiError.setErrors(Arrays.asList(String.format(ex.getError(), ex.getMerchantId())));
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(MerchantPrimaryKeyViolationException.class)
	protected ResponseEntity<Object> handleMerchantPrimaryKeyViolation(MerchantPrimaryKeyViolationException ex) {
		RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST);
		apiError.setType(MerchantPrimaryKeyViolationException.class.getSimpleName());
		apiError.setMessage(ex.getMessage());
		apiError.setErrors(Arrays.asList(String.format(ex.getError(), ex.getMerchantId())));
		return buildResponseEntity(apiError);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		RestApiError apiError = new RestApiError(HttpStatus.BAD_REQUEST);
		apiError.setType(MethodArgumentNotValidException.class.getSimpleName());
		apiError.setMessage(ExceptionMessages.OFFER_VALIDATION_EXCEPTION_MESSAGE);

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());
		
		apiError.setErrors(errors);
		return buildResponseEntity(apiError);
	}
}
