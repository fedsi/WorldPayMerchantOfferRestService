package com.worldpay.assignment.merchantoffer.specification;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.repository.OfferRepository;

import static org.springframework.data.jpa.domain.Specification.where;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsActive;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsExpired;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsCancelled;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.merchantIdIs;


@DataJpaTest
public class OfferSpecificationTest {
	
	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	
	@Test
	void givenActiveOffer_whenFindAll$offerIsActiveCalled_thenNoOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);
		testEntityManager.persist(merchant);
		testEntityManager.persist(offer);
        testEntityManager.flush();

		// When
		List<Offer> offerList = offerRepository.findAll(where(offerIsActive()));

		// Then
		assertTrue(!offerList.isEmpty());
	}
	
	@Test
	void givenEmptyRepository_whenFindAll$OfferIsExpiredIsCalled_thenNoOfferIsReturned() {
		// Given
		// Empty repository

		// When
		List<Offer> offerList = offerRepository.findAll(where(offerIsExpired()));

		// Then
		assertTrue(offerList.isEmpty());
	}
	
	@Test
	void givenActiveOffer_whenFindAll$OfferIsCancelledIsCalled_thenNoOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);
		testEntityManager.persist(merchant);
		testEntityManager.persist(offer);
        testEntityManager.flush();
        
		// When
		List<Offer> offerList = offerRepository.findAll(where(offerIsCancelled()));

		// Then
		assertTrue(offerList.isEmpty());
	}
	
	@Test
	void givenCancelledOffer_whenFindAll$OfferIsCancelledIsCalled_thenOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyCancelledOffer(merchant);
		testEntityManager.persist(merchant);
		testEntityManager.persist(offer);
        testEntityManager.flush();
        
		// When
		List<Offer> offerList = offerRepository.findAll(where(offerIsCancelled()));

		// Then
		assertTrue(!offerList.isEmpty());
		assertEquals(offer, offerList.get(0));
	}
	
	@Test
	void givenOfferLinkedToMerchant_whenFindAll$merchantEqualsToIsCalled_thenOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);
		testEntityManager.persist(merchant);
		testEntityManager.persist(offer);
        testEntityManager.flush();

		// When
		List<Offer> offerList = offerRepository.findAll(where(merchantIdIs(offer.getMerchant().getId())));

		// Then
		assertTrue(!offerList.isEmpty());
	}

	@Test
	void givenOfferLinkedToAnotherMerchant_whenFindAll$merchantEqualsToIsCalled_thenNoOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Merchant merchant2 = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);
		testEntityManager.persist(merchant);
		testEntityManager.persist(merchant2);
		testEntityManager.persist(offer);
        testEntityManager.flush();

		// When
		List<Offer> offerList = offerRepository.findAll(where(merchantIdIs(merchant2.getId())));

		// Then
		assertTrue(offerList.isEmpty());
	}
}
