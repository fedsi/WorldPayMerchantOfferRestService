package com.worldpay.assignment.merchantoffer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.repository.MerchantRepository;
import com.worldpay.assignment.merchantoffer.service.impl.MerchantServiceImpl;

@DataJpaTest
public class MerchantServiceTest {

	@InjectMocks
	private MerchantServiceImpl merchantService;

	@Mock
	private MerchantRepository merchantRepository;

	@Test
	void givenEmptyRepository_whenFindAllOMerchantsIsCalled_thenEmptyListIsReturned() {
		// Given
		given(merchantRepository.findAll()).willReturn(Collections.emptyList());

		// When
		List<Merchant> actualMerchantList = merchantService.findAllMerchants();

		// Then
		assertTrue(actualMerchantList.isEmpty());
	}

	@Test
	void givenMerchant_whenFindMerchantIsCalled_thenMerchantIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();

		given(merchantRepository.findById(anyLong())).willReturn(Optional.of(merchant));

		// When
		Optional<Merchant> actualMerchant = merchantService.findMerchantById(merchant.getId());

		// Then
		assertTrue(actualMerchant.isPresent());
		assertEquals(merchant.getId(), actualMerchant.get().getId());
	}

	@Test
	void givenMerchant_whenSaveMerchantIsCalled_thenMerchantIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();

		// When
		Merchant actualMerchant = merchantService.saveMerchant(merchant);

		// Then
		assertEquals(merchant.getName(), actualMerchant.getName());
	}
}
