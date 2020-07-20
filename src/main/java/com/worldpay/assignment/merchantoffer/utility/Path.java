package com.worldpay.assignment.merchantoffer.utility;

public class Path {

	public static final String MERCHANTS = "/merchants";
	public static final String MERCHANTS_ID = "/merchants/{merchantid}";

	
	public static final String OFFERS = "/offers";
	public static final String OFFERS_WITH_MERCHANTID = "/offers?merchantid={merchantid}";
	public static final String OFFERS_BY_STATUS = "/offers/findByStatus";
	public static final String OFFERS_BY_STATUS_WITH_MERCHANTID = "/offers/findByStatus?merchantid={merchantid}&status={status}";
	public static final String OFFERS_ID = "/offers/{offerid}";
	public static final String OFFERS_ID_WITH_MERCHANTID = "/offers/{offerid}?merchantid={merchantid}";

	
}
