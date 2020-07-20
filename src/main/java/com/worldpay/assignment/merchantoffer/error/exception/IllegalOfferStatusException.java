package com.worldpay.assignment.merchantoffer.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * This exception will be thrown when an offer has a state that doesn't allow
 * changes.
 * 
 * @author Federico Silveri
 * @since v1.0
 * 
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal offer status")
public class IllegalOfferStatusException extends MerchantOfferException {

	private static final long serialVersionUID = 3108069972696246280L;

	String offerId;

	public IllegalOfferStatusException(String message, String error, String offerId) {
		super(message, error);
		this.offerId = offerId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
}
