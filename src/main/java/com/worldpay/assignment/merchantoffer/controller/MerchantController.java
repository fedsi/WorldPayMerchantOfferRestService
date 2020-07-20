package com.worldpay.assignment.merchantoffer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.worldpay.assignment.merchantoffer.entity.Merchant;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantPrimaryKeyViolationException;
import com.worldpay.assignment.merchantoffer.service.MerchantService;

@RestController
public class MerchantController {


	@Autowired
	MerchantService merchantService;
	
	/**
	 * Return all {@link Merchant}
	 * 
	 * 
	 * @return List<Merchant> - A list with all existing merchants
	 */
	@GetMapping("/merchants")
	List<Merchant> getAllMerchant() {
		return merchantService.findAllMerchants();
	}
	
	/**
	 * Return an {@link Merchant} object
	 * 
	 * 
	 * @return Merchant - The specified merchant
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/merchants/{merchantid}")
	ResponseEntity getMerchant(@PathVariable long merchantid) {
		return new ResponseEntity(merchantService.findMerchantById(merchantid), HttpStatus.OK);
	}
	
	/**
	 * Create a new {@link Merchant}
	 * 
	 * 
	 * @return Merchant - The new merchant
	 * @throws MerchantPrimaryKeyViolationException - If the given merchantid already exist in memory database
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(path = "/merchants", produces=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity saveMerchant(@RequestBody Merchant newMerchant) {
		return new ResponseEntity(merchantService.saveMerchant(newMerchant), HttpStatus.CREATED);
	}
}
