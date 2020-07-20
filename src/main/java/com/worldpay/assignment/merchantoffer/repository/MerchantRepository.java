package com.worldpay.assignment.merchantoffer.repository;

import org.springframework.data.repository.CrudRepository;

import com.worldpay.assignment.merchantoffer.entity.Merchant;

public interface MerchantRepository extends CrudRepository<Merchant, Long> {

	
}
