package com.worldpay.assignment.merchantoffer.error.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
*
 * This exception will be thrown when an offer with the specified id
 * does not exist.
* 
* @author Federico Silveri
* @since v1.0
* 
*/
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Offer not found")
public class OfferNotFoundException extends MerchantOfferException {

	private static final long serialVersionUID = -8928197611066762161L;

	String offerId;
	
	public OfferNotFoundException(String message, String error, String offerId) {
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
