package com.worldpay.assignment.merchantoffer.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;
import com.worldpay.assignment.merchantoffer.service.MerchantService;
import com.worldpay.assignment.merchantoffer.service.OfferService;
import com.worldpay.assignment.merchantoffer.utility.Path;

@WebMvcTest(controllers = OfferController.class)
public class OfferControllerTest {

	@MockBean
	private OfferService offerService;

	@MockBean
	private MerchantService merchantService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void get_givenEmptyRepository_whenGetOffersIsCalled_thenHttpStatusIsOkAndNoOfferIsReturned()
			throws Exception {
		// Given
		List<Offer> expectedOfferList = Collections.emptyList();
		given(offerService.findAllOffers(null)).willReturn(expectedOfferList);

		// When
		MockHttpServletResponse response = mockMvc.perform(get(Path.OFFERS)).andReturn().getResponse();

		// Then
		ObjectMapper mapper = new ObjectMapper();
		List<Offer> actualOfferList = mapper.readValue(response.getContentAsString(), new TypeReference<List<Offer>>() {
		});

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(expectedOfferList, actualOfferList);
	}

	@Test
	void get_givenOffer_whenGetOffersIsCalled_thenHttpStatusIsOkAndOfferIsReturned() throws Exception {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);
		String offerId = offer.getId();
		long merchantid = merchant.getId();
		Optional<Offer> optOffer = Optional.ofNullable(offer);

		given(offerService.findOffer(anyLong(), anyString())).willReturn(optOffer);

		// When
		MockHttpServletResponse response = mockMvc.perform(get(Path.OFFERS_ID_WITH_MERCHANTID, offerId, merchantid))
				.andReturn().getResponse();

		// Then
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		Offer actualOffer = mapper.readValue(response.getContentAsString(), Offer.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(offer, actualOffer);
	}

	@Test
	void get_givenActiveOffers_whenGetOffersByStatusIsCalled_thenHttpStatusIsOkActiveOfferIsReturned()
			throws Exception {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		long merchantid = merchant.getId();

		Offer activeOffer = DummyEntityFactory.createDummyActiveOffer(merchant);

		List<Offer> expectedOfferList = Collections.singletonList(activeOffer);

		given(offerService.findOffersByStatus(anyLong(), anyString())).willReturn(expectedOfferList);

		// When
		MockHttpServletResponse response = mockMvc
				.perform(get(Path.OFFERS_BY_STATUS_WITH_MERCHANTID, merchantid, OfferStatus.ACTIVE.toString()))
				.andReturn().getResponse();

		// Then
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		List<Offer> actualOfferList = mapper.readValue(response.getContentAsString(), new TypeReference<List<Offer>>() {
		});

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(OfferStatus.ACTIVE, actualOfferList.get(0).getStatus());
	}

	@Test
	void post_givenNewOffer_whenSaveOfferCalled_thenHttpStatusIsCreated() throws Exception {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();

		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		given(offerService.saveMerchantOffer(merchant.getId(), offer)).willReturn(offer);

		// When
		MockHttpServletResponse response = mockMvc.perform(post(Path.OFFERS_WITH_MERCHANTID, merchant.getId())
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(offer))).andReturn()
				.getResponse();

		// Then
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

	}

	@Test
	void update_givenUpdatedOffer_whenUpdateOfferIsCalled_thenHttpStatusIsOkAndUpdatedOfferIsReturned()
			throws Exception {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		Offer updatedOffer = offer;
		offer.setDescription("Updated description");

		given(offerService.updateMerchantOffer(merchant.getId(), offer.getId(), offer)).willReturn(updatedOffer);

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		// When
		MockHttpServletResponse response = mockMvc
				.perform(put(Path.OFFERS_ID_WITH_MERCHANTID, offer.getId(), merchant.getId())
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(offer)))
				.andReturn().getResponse();

		// Then
		mapper.registerModule(new Jackson2HalModule());  //used to handle _link field properly

		Offer actualUpdatedOffer = mapper.readValue(response.getContentAsString(), Offer.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals("Updated description", actualUpdatedOffer.getDescription());
	}

	@Test
	void delete_givenOfferId_whenDeleteOfferIsCalled_thenHttpStatusIsOkAndOfferStatusIsCancelled() throws Exception {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchant();
		Offer offer = DummyEntityFactory.createDummyActiveOffer(merchant);

		offer.setStatus(OfferStatus.CANCELLED);

		given(offerService.cancelOffer(anyLong(), anyString())).willReturn(offer);

		// When
		MockHttpServletResponse response = mockMvc
				.perform(delete(Path.OFFERS_ID_WITH_MERCHANTID, offer.getId(), merchant.getId())).andReturn()
				.getResponse();

		// Then
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jackson2HalModule()); //used to handle _link field properly

		Offer actualOffer = mapper.readValue(response.getContentAsString(), Offer.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(OfferStatus.CANCELLED, actualOffer.getStatus());
	}
}
