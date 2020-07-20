package com.worldpay.assignment.merchantoffer.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.worldpay.assignment.merchantoffer.entity.Offer;

public interface OfferRepository extends CrudRepository<Offer, String> , JpaSpecificationExecutor<Offer> {

}
