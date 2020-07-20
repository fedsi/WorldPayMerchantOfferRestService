package com.worldpay.assignment.merchantoffer.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * This exception will be thrown when a merchant with the specified id
 * does not exist.
 * 
 * @author Federico Silveri
 * @since v1.0
 * 
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Merchant not found")
public class MerchantNotFoundException extends MerchantOfferException {

	private static final long serialVersionUID = 3453811047800501724L;

	long merchantid;

	public MerchantNotFoundException(String message, String error, long merchantid) {
		super(message, error);
		this.merchantid = merchantid;
	}

	public long getMerchantId() {
		return merchantid;
	}

	public void setMerchantId(long merchantid) {
		this.merchantid = merchantid;
	}

}
