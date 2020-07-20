package com.worldpay.assignment.merchantoffer.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;

@DataJpaTest
public class MerchantRepositoryTest {

	private Merchant dummyMerchantDTO;

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@BeforeEach
	public void initTestVariables() {
		init();
	}

	private void init() {
		dummyMerchantDTO = DummyEntityFactory.createDummyMerchant();
	}

	@Test
	public void givenNewMerchant_whenMerchantIsSaved_thenItCanBeRetrieved() {
		// Given
		testEntityManager.persist(dummyMerchantDTO);
		testEntityManager.flush();

		// when
		merchantRepository.save(dummyMerchantDTO);

		// Then
		Optional<Merchant> merchant = merchantRepository.findById(dummyMerchantDTO.getId());

		assertTrue(merchant.isPresent());
		assertEquals(dummyMerchantDTO.getId(), merchant.get().getId());
	}
}
