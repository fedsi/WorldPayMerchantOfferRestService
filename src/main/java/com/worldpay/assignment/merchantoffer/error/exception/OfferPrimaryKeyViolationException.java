package com.worldpay.assignment.merchantoffer.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
*
* This exception will be thrown when an offer with the specified id
* already exist.
* 
* @author Federico Silveri
* @since v1.0
* 
*/
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Offer with same id already exist")
public class OfferPrimaryKeyViolationException extends MerchantOfferException {

	private static final long serialVersionUID = 2184651837449631307L;
	String offerId;
	
	public OfferPrimaryKeyViolationException(String message, String error, String offerId) {
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
