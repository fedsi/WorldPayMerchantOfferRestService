package com.worldpay.assignment.merchantoffer.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
*
* This exception will be thrown when the provided offer object has one (or more) field invalid.
* 
* @author Federico Silveri
* @since v1.0
* 
*/
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid offer")
public class InvalidOfferException extends MerchantOfferException {

	private static final long serialVersionUID = 7126079752107242051L;

	public InvalidOfferException(String message, String error) {
		super(message, error);
	}

	
}
