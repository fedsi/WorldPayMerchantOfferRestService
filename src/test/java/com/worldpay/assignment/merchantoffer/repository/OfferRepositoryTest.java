package com.worldpay.assignment.merchantoffer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.entity.Offer;

@DataJpaTest
public class OfferRepositoryTest {

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	Merchant dummyMerchant;
	
	@BeforeEach
	public void initTestVariables() {
		init();
	}

	private void init() {
		Merchant dummyMerchant = DummyEntityFactory.createDummyMerchant();
		testEntityManager.persist(dummyMerchant);
		testEntityManager.flush();

	}

	/**
	 * Test {@link OfferRepository#save(Object)} with an {@link Offer} with blank
	 * description.
	 */
	@Test
	void givenBlankDescription_whenOfferIsPersisted_thenConstraintViolationExceptionIsThrown() {
		// Given
		Offer dummyOffer = DummyEntityFactory.createDummyActiveOffer(dummyMerchant);

		testEntityManager.persist(dummyOffer);
		dummyOffer.setDescription("");

		// When/Then
		assertThrows(ConstraintViolationException.class, () -> {
			offerRepository.save(dummyOffer);
			testEntityManager.flush();
		});
	}

	/**
	 * Test {@link OfferRepository#save(Object)} with a correct {@link Offer}
	 * object.
	 */
	@Test
	public void givenLegitOffer_whenOfferIsSaved_thenItCanBeRetrieved() {
		// Given
		Offer dummyOffer = DummyEntityFactory.createDummyActiveOffer(dummyMerchant);
		testEntityManager.persist(dummyOffer);
		testEntityManager.flush();

		// when
		offerRepository.save(dummyOffer);

		// Then
		Optional<Offer> offer = offerRepository.findById(dummyOffer.getId());

		assertTrue(offer.isPresent());
		assertEquals(dummyOffer.getId(), offer.get().getId());
	}

}
