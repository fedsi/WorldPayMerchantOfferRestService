package com.worldpay.assignment.merchantoffer.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;

public class OfferSpecification {

	public static Specification<Offer> offerIsActive() {
		return (root, query, criteriaBuilder) -> {
	        return criteriaBuilder.greaterThan(root.get("expirationDate"), LocalDateTime.now());
	    };
	}
	
	public static Specification<Offer> offerIsExpired() {
		return (root, query, criteriaBuilder) -> {
	        return criteriaBuilder.lessThanOrEqualTo(root.get("expirationDate"), LocalDateTime.now());
	    };
	}
	
	public static Specification<Offer> offerIsCancelled() {
		return (root, query, criteriaBuilder) -> {
	        return criteriaBuilder.equal(root.get("status"), OfferStatus.CANCELLED);
	    };
	}

	public static Specification<Offer> merchantIdIs(long merchantId) {
		return (root, query, criteriaBuilder) -> {
	        return criteriaBuilder.equal(root.join("merchant").get("id"), merchantId);
	    };
	}
	
	public static Specification<Offer> offerIdIs(String offerId) {
		return (root, query, criteriaBuilder) -> {
	        return criteriaBuilder.equal(root.get("id"), offerId);
	    };
	}
}
