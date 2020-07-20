package com.worldpay.assignment.merchantoffer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantPrimaryKeyViolationException;
import com.worldpay.assignment.merchantoffer.error.message.ExceptionMessages;
import com.worldpay.assignment.merchantoffer.repository.MerchantRepository;
import com.worldpay.assignment.merchantoffer.service.MerchantService;

@Service
public class MerchantServiceImpl implements MerchantService {

	private static final Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

	@Autowired
	MerchantRepository merchantRepo;

	/**
     * {@inheritDoc}
     */
	@Override
	public Optional<Merchant> findMerchantById(long id) {
		Optional<Merchant> merchant = merchantRepo.findById(id);
		return merchant;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public List<Merchant> findAllMerchants() {
		List<Merchant> merchantList = new ArrayList<Merchant>();
		merchantRepo.findAll().forEach(merchantList::add);
        return merchantList;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public Merchant saveMerchant(Merchant newMerchant) {
		if(findMerchantById(newMerchant.getId()).isPresent())
			throw new MerchantPrimaryKeyViolationException(ExceptionMessages.MERCHANT_PRIMARY_KEY_VIOLATION_EXCEPTION_MESSAGE, ExceptionMessages.MERCHANT_PRIMARY_KEY_VIOLATION_EXCEPTION_ERROR, newMerchant.getId());
		merchantRepo.save(newMerchant);
		log.info(String.format("Merchant with id %d SUCCESFULLY CREATED", newMerchant.getId()));
		return newMerchant;
	}
}
