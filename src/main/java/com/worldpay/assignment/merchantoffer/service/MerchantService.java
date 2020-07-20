package com.worldpay.assignment.merchantoffer.service;

import java.util.List;
import java.util.Optional;

import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantPrimaryKeyViolationException;


public interface MerchantService {

	/**
	 * Return all {@link Merchant}
	 * 
	 * 
	 * @return List<Merchant> - A list with all existing merchants
	 */
	public List<Merchant> findAllMerchants();
	
	/**
	 * Return an {@link Merchant} object
	 * 
	 * 
	 * @return Merchant - The specified merchant
	 */
	public Optional<Merchant> findMerchantById(long id);

	/**
	 * Create a new {@link Merchant}
	 * 
	 * 
	 * @return Merchant - The new merchant
	 * @throws MerchantPrimaryKeyViolationException - If the given merchantid already exist in memory database
	 */
	public Merchant saveMerchant(Merchant merchant);
}
