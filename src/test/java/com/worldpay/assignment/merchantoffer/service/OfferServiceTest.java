package com.worldpay.assignment.merchantoffer.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.jpa.domain.Specification.not;
import static org.springframework.data.jpa.domain.Specification.where;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsCancelled;
import static com.worldpay.assignment.merchantoffer.specification.OfferSpecification.offerIsExpired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;
import com.worldpay.assignment.merchantoffer.error.exception.IllegalOfferStatusException;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantNotFoundException;
import com.worldpay.assignment.merchantoffer.repository.MerchantRepository;
import com.worldpay.assignment.merchantoffer.repository.OfferRepository;
import com.worldpay.assignment.merchantoffer.service.impl.MerchantServiceImpl;
import com.worldpay.assignment.merchantoffer.service.impl.OfferServiceImpl;

@SpringBootTest
@Transactional
public class OfferServiceTest {

	@InjectMocks
	private OfferServiceImpl offerService;

	@Mock
	private MerchantServiceImpl merchantService;

	@Mock
	private OfferRepository offerRepository;

	@Mock
	private MerchantRepository merchantRepository;

//	  ______ _____ _   _ _____  
//	 |  ____|_   _| \ | |  __ \ 
//	 | |__    | | |  \| | |  | |
//	 |  __|   | | | . ` | |  | |
//	 | |     _| |_| |\  | |__| |
//	 |_|    |_____|_| \_|_____/                             

	@Test
	void givenOffer_whenFindAllOffersWithUnsavedMerchantIdIsCalled_thenMerchantNotFoundIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Merchant otherMerchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		merchantRepository.save(merchant);
		offerRepository.save(offer);

		// When/Then
		assertThrows(MerchantNotFoundException.class, () -> offerService.findAllOffers(otherMerchant.getId()));
	}

	@Test
	void givenOffer_whenFindOffersIsCalled_thenOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		given(offerRepository.findById(anyString())).willReturn(Optional.of(offer));

		// When
		Optional<Offer> actualOffer = offerService.findOffer(null, offer.getId());

		// Then
		assertTrue(actualOffer.isPresent());
		assertEquals(offer.getId(), actualOffer.get().getId());
	}

	@Test
	void givenActiveOffer_whenFindOffersByStatusWithExpiredStatusIsCalled_thenNoOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyExpiredOffer(merchant);
		offerRepository.save(offer);

		List<Offer> offerList = Collections.emptyList();

		given(offerRepository.findAll(where(offerIsExpired().and(not(offerIsCancelled()))))).willReturn(offerList);

		// When
		List<Offer> actualOfferList = offerService.findOffersByStatus(null, OfferStatus.EXPIRED.toString());

		// Then
		assertTrue(actualOfferList.isEmpty());
	}

	@Test
	void givenCancelledOffer_whenFindOffersByStatusWithCancelledStatusIsCalled_thenOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyCancelledOffer(merchant);

		List<Offer> offerList = Collections.singletonList(offer);

		given(offerRepository.findAll(where(offerIsCancelled()))).willReturn(offerList);

		// When
		List<Offer> actualOfferList = offerService.findOffersByStatus(null, OfferStatus.CANCELLED.toString());

		// Then
		assertTrue(!actualOfferList.isEmpty());
		assertEquals(OfferStatus.CANCELLED, actualOfferList.get(0).getStatus());
	}

//	   _____    __      ________ 
//	  / ____|  /\ \    / /  ____|
//	 | (___   /  \ \  / /| |__   
//	  \___ \ / /\ \ \/ / |  __|  
//	  ____) / ____ \  /  | |____ 
//	 |_____/_/    \_\/   |______|

	@Test
	public void givenOffer_whenSaveIsInvoked_thenSavedOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		given(merchantService.findMerchantById(anyLong())).willReturn(Optional.of(merchant));

		// When
		Offer savedOffer = offerService.saveMerchantOffer(merchant.getId(), offer);

		// Then
		assertThat(savedOffer).isNotNull();
		assertEquals(offer.getId(), savedOffer.getId());
		assertEquals(offer.getDescription(), savedOffer.getDescription());
		assertEquals(offer.getPrice(), savedOffer.getPrice());
		assertEquals(offer.getCurrency(), savedOffer.getCurrency());
		assertEquals(offer.getExpirationDate(), savedOffer.getExpirationDate());
	}

	@Test
	public void givenOfferWithWrongMerchant_whenSaveIsInvoked_thenMerchantNotFoundIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		given(merchantService.findMerchantById(anyLong())).willReturn(Optional.empty());

		// When - Then
		assertThatThrownBy(() -> offerService.saveMerchantOffer(merchant.getId(), offer))
				.isInstanceOf(MerchantNotFoundException.class);
	}

//	  _    _ _____  _____       _______ ______ 
//	 | |  | |  __ \|  __ \   /\|__   __|  ____|
//	 | |  | | |__) | |  | | /  \  | |  | |__   
//	 | |  | |  ___/| |  | |/ /\ \ | |  |  __|  
//	 | |__| | |    | |__| / ____ \| |  | |____ 
//	  \____/|_|    |_____/_/    \_\_|  |______|

	@Test
	public void givenExpiredOffer_whenUpdateOfferIsInvoked_thenIllegalOfferStatusIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
		Offer offer = DummyEntityFactory.createDummyExpiredOffer(merchant);

		given(merchantService.findMerchantById(merchant.getId())).willReturn(Optional.of(merchant));

		given(offerRepository.findOne(any())).willReturn(Optional.of(offer));

		offer.setDescription("Updated description");

		// When - Then
		assertThatThrownBy(() -> offerService.updateMerchantOffer(merchant.getId(), offer.getId(), offer))
				.isInstanceOf(IllegalOfferStatusException.class);
	}

	@Test
	public void givenActiveOffer_whenUpdateOfferIsInvoked_thenUpdatedOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		given(merchantService.findMerchantById(anyLong())).willReturn(Optional.of(merchant));

		given(offerRepository.findOne(any())).willReturn(Optional.of(offer));

		offer.setDescription("Updated description");

		// When -
		Offer actualOffer = offerService.updateMerchantOffer(merchant.getId(), offer.getId(), offer);
		// Then
		assertEquals(offer.getId(), actualOffer.getId());
		assertEquals(offer.getDescription(), actualOffer.getDescription());
	}

//	  _____          _   _  _____ ______ _      
//	 / ____|   /\   | \ | |/ ____|  ____| |     
//	| |       /  \  |  \| | |    | |__  | |     
//	| |      / /\ \ | . ` | |    |  __| | |     
//	| |____ / ____ \| |\  | |____| |____| |____ 
//	 \_____/_/    \_\_| \_|\_____|______|______|

	@Test
	public void givenExpiredOffer_whenCancelOfferIsInvoked_thenIllegalOfferStatusIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
		Offer offer = DummyEntityFactory.createDummyExpiredOffer(merchant);

		given(merchantService.findMerchantById(anyLong())).willReturn(Optional.of(merchant));

		given(offerRepository.findOne(any())).willReturn(Optional.of(offer));

		offer.setDescription("Updated description");

		// When - Then
		assertThatThrownBy(() -> offerService.cancelOffer(merchant.getId(), offer.getId()))
				.isInstanceOf(IllegalOfferStatusException.class);
	}

	@Test
	public void givenActiveOffer_whenCancelOfferIsInvoked_thenCancelledOfferIsReturned() {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		given(merchantService.findMerchantById(anyLong())).willReturn(Optional.of(merchant));

		given(offerRepository.findOne(any())).willReturn(Optional.of(offer));

		// When -
		Offer actualOffer = offerService.cancelOffer(merchant.getId(), offer.getId());
		// Then
		assertEquals(offer.getId(), actualOffer.getId());
		assertEquals(OfferStatus.CANCELLED, actualOffer.getStatus());
	}
}
