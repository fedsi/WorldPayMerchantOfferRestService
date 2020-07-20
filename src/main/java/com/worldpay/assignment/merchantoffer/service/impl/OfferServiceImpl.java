package com.worldpay.assignment.merchantoffer.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;
import com.google.common.base.Enums;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantNotFoundException;
import com.worldpay.assignment.merchantoffer.error.exception.IllegalOfferStatusException;
import com.worldpay.assignment.merchantoffer.error.exception.OfferNotFoundException;
import com.worldpay.assignment.merchantoffer.error.message.ExceptionMessages;
import com.worldpay.assignment.merchantoffer.repository.OfferRepository;
import com.worldpay.assignment.merchantoffer.service.MerchantService;
import com.worldpay.assignment.merchantoffer.service.OfferService;

import static org.springframework.data.jpa.domain.Specification.where;
import static org.springframework.data.jpa.domain.Specification.not;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsActive;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsExpired;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsCancelled;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.merchantIdIs;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIdIs;

@Service
public class OfferServiceImpl implements OfferService {

	private static final Logger log = LoggerFactory.getLogger(OfferServiceImpl.class);

	@Autowired
	OfferRepository offerRepo;

	@Autowired
	MerchantService merchantService;

	/**
     * {@inheritDoc}
     */
	@Override
	public List<Offer> findAllOffers(Long merchantid) {
		List<Offer> offerList = new ArrayList<Offer>();
		if (merchantid != null) {
			checkExistMerchant(merchantid);

			offerRepo.findAll(where(merchantIdIs(merchantid))).forEach(offerList::add);
		} else {
			offerRepo.findAll().forEach(offerList::add);
		}

		for (Offer offer : offerList) {
			expireOffer(offer);
		}
		return offerList;
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public Optional<Offer> findOffer(Long merchantid, String offerId) {
		Optional<Offer> offer = Optional.ofNullable(null);
		if (merchantid != null) {
			checkExistMerchant(merchantid);

			offer = offerRepo.findOne(where(merchantIdIs(merchantid)).and(offerIdIs(offerId)));
		} else {
			offer = offerRepo.findById(offerId);
		}

		if (offer.isPresent()) {
			expireOffer(offer.get());
		}
		return offer;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public List<Offer> findOffersByStatus(Long merchantid, String status) {
		OfferStatus statusEnum = Enums.getIfPresent(OfferStatus.class, status.toUpperCase()).orNull();
		if (statusEnum != null) {
			switch (statusEnum) {
			case ACTIVE:
				return findActiveOffers(merchantid);
			case EXPIRED:
				return findExpiredOffers(merchantid);
			case CANCELLED:
				return findCancelledOffers(merchantid);
			}
		}
		// Unrecognized status
		throw new IllegalOfferStatusException(ExceptionMessages.ILLEGAL_OFFER_STATUS_EXCEPTION_MESSAGE,
				ExceptionMessages.ILLEGAL_OFFER_STATUS_EXCEPTION_SELECT_ERROR, status);
	}

	public List<Offer> findActiveOffers(Long merchantid) {
		List<Offer> offerList = new ArrayList<Offer>();
		if (merchantid != null) {
			checkExistMerchant(merchantid);

			offerRepo.findAll(where(merchantIdIs(merchantid).and(offerIsActive().and(not(offerIsCancelled()))))).forEach(offerList::add);
		} else {
			offerRepo.findAll(where(offerIsActive().and(not(offerIsCancelled())))).forEach(offerList::add);
		}

		return offerList;
	}

	public List<Offer> findExpiredOffers(Long merchantid) {
		List<Offer> offerList = new ArrayList<Offer>();
		if (merchantid != null) {

			checkExistMerchant(merchantid);

			offerRepo.findAll(where(merchantIdIs(merchantid).and(offerIsExpired().and(not(offerIsCancelled()))))).forEach(offerList::add);
		} else {
			offerRepo.findAll(where(offerIsExpired().and(not(offerIsCancelled())))).forEach(offerList::add);
		}
		return offerList;
	}

	public List<Offer> findCancelledOffers(Long merchantid) {
		List<Offer> offerList = new ArrayList<Offer>();
		if (merchantid != null) {
			checkExistMerchant(merchantid);

			offerRepo.findAll(where(merchantIdIs(merchantid).and(offerIsCancelled()))).forEach(offerList::add);
		} else {
			offerRepo.findAll(where(offerIsCancelled())).forEach(offerList::add);
		}
		return offerList;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public Offer saveMerchantOffer(long merchantid, Offer newOffer) {
		Merchant merchant = checkExistMerchant(merchantid);

		if (newOffer.getId() == null || "".equals(newOffer.getId()))
			newOffer.setId(UUID.randomUUID().toString());
		if (newOffer.getCreationDate() == null)
			newOffer.setCreationDate(LocalDateTime.now());
		newOffer.setMerchant(merchant);

		expireOffer(newOffer);
		offerRepo.save(newOffer);
		log.info(String.format("Offer with id %s CREATED SUCCESFULLY for merchant %s", newOffer.getId(),
				merchant.getName()));
		return newOffer;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public Offer updateMerchantOffer(Long merchantid, String offerid, Offer offer) {
		Merchant merchant = checkExistMerchant(merchantid);
		
		Optional<Offer> oldOffer = findOffer(merchantid, offerid);

		if (!oldOffer.isPresent()) {
			throw new OfferNotFoundException(ExceptionMessages.OFFER_NOT_FOUND_EXCEPTION_MESSAGE,
					ExceptionMessages.OFFER_NOT_FOUND_EXCEPTION_ERROR, offerid);
		}

		if (OfferStatus.EXPIRED.equals(oldOffer.get().getStatus())) {
			throw new IllegalOfferStatusException(ExceptionMessages.ILLEGAL_OFFER_STATUS_EXCEPTION_MESSAGE,
					ExceptionMessages.ILLEGAL_OFFER_STATUS_EXCEPTION_EDIT_ERROR, offerid);
		}

		offer.setId(oldOffer.get().getId());
		offer.setMerchant(merchant);
		expireOffer(offer);
		offerRepo.save(offer);
		log.info(String.format("Offer with id %s for merchant %s UPDATED", offer.getId(), merchant.getName()));

		return offer;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public Offer cancelOffer(Long merchantid, String offerid) {
		Optional<Offer> offer = findOffer(merchantid, offerid);
		if (!offer.isPresent()) {
			throw new OfferNotFoundException(ExceptionMessages.OFFER_NOT_FOUND_EXCEPTION_MESSAGE,
					ExceptionMessages.OFFER_NOT_FOUND_EXCEPTION_MESSAGE, offerid);
		}
		if (OfferStatus.EXPIRED.equals(offer.get().getStatus())) {
			throw new IllegalOfferStatusException(ExceptionMessages.ILLEGAL_OFFER_STATUS_EXCEPTION_MESSAGE,
					ExceptionMessages.ILLEGAL_OFFER_STATUS_EXCEPTION_CANCEL_ERROR, offerid);
		} else {
			offer.get().setStatus(OfferStatus.CANCELLED);
			offerRepo.save(offer.get());
		}

		log.info(String.format("Offer with id %s CANCELLED", offerid));
		return offer.get();
	}

	private Offer expireOffer(Offer offer) {
		if (!OfferStatus.CANCELLED.equals(offer.getStatus())) {
			if (LocalDateTime.now().isAfter(offer.getExpirationDate())) {
				offer.setStatus(OfferStatus.EXPIRED);
				offerRepo.save(offer);
			} else {
				offer.setStatus(OfferStatus.ACTIVE);
				offerRepo.save(offer);
			}
		}
		return offer;
	}
	
	private Merchant checkExistMerchant(Long merchantid) {
		Optional<Merchant> merchant = merchantService.findMerchantById(merchantid);
		if (!merchant.isPresent()) {
			throw new MerchantNotFoundException(ExceptionMessages.MERCHANT_NOT_FOUND_EXCEPTION_MESSAGE,
					ExceptionMessages.MERCHANT_NOT_FOUND_EXCEPTION_ERROR, merchantid);
		}
			
		return merchant.get();
	}
}
