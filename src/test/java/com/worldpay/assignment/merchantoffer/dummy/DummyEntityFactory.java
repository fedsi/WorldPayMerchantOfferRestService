package com.worldpay.assignment.merchantoffer.dummy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;

public class DummyEntityFactory  {

	public static Merchant createDummyMerchant() {
		return new Merchant("Merchant Name");
	}
	
	public static Merchant createDummyMerchantWithId() {
		return new Merchant(1, "Merchant Name");
	}

	public static Offer createDummyActiveOffer(Merchant merchant) {
		return new Offer("Best offer",  new BigDecimal(100), "EUR", LocalDateTime.now().plusMinutes(10), OfferStatus.ACTIVE, merchant);
	}
	
	public static Offer createDummyActiveOffer() {
		return new Offer("Best offer",  new BigDecimal(100), "EUR", LocalDateTime.now().plusMinutes(10), OfferStatus.ACTIVE, null);
	}
	
	public static Offer createDummyExpiredOffer(Merchant merchant) {
		return new Offer("Best offer",  new BigDecimal(100), "EUR", LocalDateTime.now().minusMinutes(10), OfferStatus.EXPIRED, merchant);
	}
	
	public static Offer createDummyCancelledOffer(Merchant merchant) {
		return new Offer("Best offer",  new BigDecimal(100), "EUR", LocalDateTime.now().plusMinutes(10), OfferStatus.CANCELLED, merchant);
	}
}
