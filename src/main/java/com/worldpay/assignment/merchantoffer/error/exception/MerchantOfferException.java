package com.worldpay.assignment.merchantoffer.error.exception;

/**
* @author Federico Silveri
* @since v1.0
* 
*/
public class MerchantOfferException extends RuntimeException {

	private static final long serialVersionUID = 5776263944507202002L;
	String error;
	
	public MerchantOfferException(String message, String error) {
		super(message);
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
