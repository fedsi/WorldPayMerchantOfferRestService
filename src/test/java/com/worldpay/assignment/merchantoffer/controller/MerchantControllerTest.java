package com.worldpay.assignment.merchantoffer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.worldpay.assignment.merchantoffer.dummy.DummyEntityFactory;
import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.service.MerchantService;
import com.worldpay.assignment.merchantoffer.utility.Path;

@WebMvcTest(controllers = MerchantController.class)
public class MerchantControllerTest {
	
	@MockBean
	private MerchantService merchantService;
	
	@Autowired
	private MockMvc mockMvc;
	
	/**
	 * Test {@link MerchantController#getAllMerchant()} with an empty repository.
	 */
	@Test
	public void get_givenEmptyRepository_whenGetMerchantsIsCalled_thenHttpStatusIsOkAndNoMerchantIsReturned() throws Exception {
		// Given
		List<Merchant> expectedMerchantList = Collections.emptyList();
		given(merchantService.findAllMerchants()).willReturn(expectedMerchantList);
		
		// When
		MockHttpServletResponse response = mockMvc.perform(get(Path.MERCHANTS)
                .contentType(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse();

		//Then
		ObjectMapper mapper = new ObjectMapper();
		List<Merchant> actualMerchantList = mapper.readValue(response.getContentAsString(),
				new TypeReference<List<Merchant>>() {
				});
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(expectedMerchantList, actualMerchantList);
	}
	
	/**
	 * Test {@link MerchantController#getMerchant(long)} with a {@link Merchant} in the repository.
	 */
	@Test
    void get_givenMerchant_whenGetMerchantsIsCalled_thenHttpStatusIsOkAndMerchantIsReturned() throws Exception {
        // Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
		long merchantId = merchant.getId();
		Optional<Merchant> optMerchant = Optional.ofNullable(merchant);
		
        given(merchantService.findMerchantById(anyLong()))
                .willReturn(optMerchant);

        // When
        MockHttpServletResponse response = mockMvc.perform(get(Path.MERCHANTS_ID,  merchantId)
                .contentType(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse();

        // Then
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		Merchant actualMerchant = mapper.readValue(response.getContentAsString(), Merchant.class);
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(merchant, actualMerchant);
    }
	
	/**
	 * Test {@link MerchantController#saveMerchant(Merchant)} with a {@link Merchant} object.
	 */
	@Test
	public void post_givenMerchant_whenPostMerchantIsCalled_thenResponseIs201()throws Exception {
		// Given
		Merchant merchant = DummyEntityFactory.createDummyMerchantWithId();
        ObjectMapper mapper = new ObjectMapper();

        // When
        MockHttpServletResponse response = mockMvc.perform(post(Path.MERCHANTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(merchant)))
        		.andReturn()
        		.getResponse();

        // Then
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
	}
}
