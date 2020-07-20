package com.worldpay.assignment.merchantoffer.service;

import java.util.List;
import java.util.Optional;

import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;
import com.worldpay.assignment.merchantoffer.error.exception.IllegalOfferStatusException;
import com.worldpay.assignment.merchantoffer.error.exception.InvalidOfferException;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantNotFoundException;
import com.worldpay.assignment.merchantoffer.error.exception.OfferNotFoundException;
import com.worldpay.assignment.merchantoffer.error.exception.OfferPrimaryKeyViolationException;

public interface OfferService {
	
	/**
	 * Return a list of {@link Offer}
	 * 
	 * 
	 * @return List<{@link Offer}>
	 */
	public List<Offer> findAllOffers(Long merchantid);
		
	/**
	 * Create a new {@link Offer}
	 * 
	 * 
	 * @return {@link Offer} 
	 * 
	 * @throws MerchantNotFoundException When the merchant does not exist
	 * @throws InvalidOfferException When the provided offer object is invalid
	 */
	public Offer saveMerchantOffer(long merchantid, Offer offer);
	
	/**
	 * Create a new {@link Offer}
	 * 
	 * 
	 * @return List<{@link Offer}>
	 * 
	 * @throws MerchantNotFoundException When the merchant does not exist
	 * @throws InvalidOfferException When the provided offer object is invalid
	 */
	public List<Offer> findOffersByStatus(Long merchantid, String status);
		
	/**
	 * Return an {@link Offer} object.
	 * 
	 * @return {@link Offer} 
	 * 
	 * @throws MerchantNotFoundException When the merchant does not exist
	 */
	public Optional<Offer> findOffer(Long merchantid, String offerId);
	
	/**
	 * Update an {@link Offer}
	 * 
	 * 
	 * @return Offer - The updated offer
	 * 
	 * @throws MerchantNotFoundException When the merchant does not exist
	 * @throws OfferNotFoundException When the offer does not exist
	 * @throws OfferPrimaryKeyViolationException When the offer already exist
	 * @throws InvalidOfferException When the provided offer object is invalid
	 * @throws IllegalOfferStatusException When an offer with the specified id has an invalid state
	 */
	public Offer updateMerchantOffer(Long merchantid, String offerId, Offer offer);

	/**
	 * 
	 * Change the status of an existing {@link Offer} to {@link OfferStatus#CANCELLED}
	 * 
	 * 
	 * @return Offer - The given offer with CANCELLED status
	 * 
	 * @throws OfferNotFoundException When the offer does not exist
	 * @throws IllegalOfferStatusException When the offer is already expired
	 */
	public Offer cancelOffer(Long merchantid, String offerid);	
}
