package com.worldpay.assignment.merchantoffer.error.message;

public class ExceptionMessages {

	/**
	 * OFFER MESSAGES - ERRORS
	 */
	public static final String OFFER_NOT_FOUND_EXCEPTION_MESSAGE ="Unable to find offer";
	public static final String OFFER_NOT_FOUND_EXCEPTION_ERROR ="Offer with id %s doesn't exist";
	
	public static final String OFFER_PRIMARY_KEY_VIOLATION_EXCEPTION_MESSAGE = "Offer already exist";
	public static final String OFFER_PRIMARY_KEY_VIOLATION_EXCEPTION_ERROR = "Offer with id % already exist";
	
	public static final String OFFER_VALIDATION_EXCEPTION_MESSAGE ="Validation Failed";
	
	public static final String ILLEGAL_OFFER_STATUS_EXCEPTION_MESSAGE = "Illegal offer status";
	public static final String ILLEGAL_OFFER_STATUS_EXCEPTION_EDIT_ERROR = "Unable to update offer with id %s. It's already expired";
	public static final String ILLEGAL_OFFER_STATUS_EXCEPTION_CANCEL_ERROR = "Unable to cancel offer with id %s. It's already expired";
	public static final String ILLEGAL_OFFER_STATUS_EXCEPTION_SELECT_ERROR = "Unrecognized status: %s";

	/**
	 * MERCHANT MESSAGES - ERRORS
	 */
	public static final String MERCHANT_NOT_FOUND_EXCEPTION_MESSAGE ="Unable to find merchant";
	public static final String MERCHANT_NOT_FOUND_EXCEPTION_ERROR ="Merchant with id %d doesn't exist";
	
	public static final String MERCHANT_PRIMARY_KEY_VIOLATION_EXCEPTION_MESSAGE = "Merchant already exist";
	public static final String MERCHANT_PRIMARY_KEY_VIOLATION_EXCEPTION_ERROR = "Merchant with id %d already exist";

}
