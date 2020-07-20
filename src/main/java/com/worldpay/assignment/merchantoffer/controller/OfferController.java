package com.worldpay.assignment.merchantoffer.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.worldpay.assignment.merchantoffer.entity.Offer;
import com.worldpay.assignment.merchantoffer.entity.OfferStatus;
import com.worldpay.assignment.merchantoffer.error.exception.InvalidOfferException;
import com.worldpay.assignment.merchantoffer.error.exception.MerchantNotFoundException;
import com.worldpay.assignment.merchantoffer.error.exception.IllegalOfferStatusException;
import com.worldpay.assignment.merchantoffer.error.exception.OfferNotFoundException;
import com.worldpay.assignment.merchantoffer.error.exception.OfferPrimaryKeyViolationException;
import com.worldpay.assignment.merchantoffer.service.OfferService;
import com.worldpay.assignment.merchantoffer.utility.Path;

import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OfferController {

	@Autowired
	OfferService offerService;

	/**
	 * Return a list of {@link Offer}
	 * 
	 * 
	 * @return A list with all existing offers
	 */
	@GetMapping(Path.OFFERS)
	List<Offer> findAllOffers(@RequestParam(name = "merchantid", required = false) Long merchantid) {
		List<Offer> offerList = offerService.findAllOffers(merchantid);
		for (final Offer offer : offerList) {
			Link selfLink = linkTo(methodOn(OfferController.class).getOffer(offer.getId(), merchantid)).withSelfRel()
					.expand();
			offer.add(selfLink);
		}
		return offerList;
	}

	/**
	 * Create a new {@link Offer}
	 * 
	 * 
	 * @return Offer - The new created offer
	 * 
	 * @throws MerchantNotFoundException When a merchant with the specified id does
	 *                                   not exist
	 * @throws InvalidOfferException     When the provided offer object is invalid
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(path = Path.OFFERS, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity saveOffer(@RequestParam(name = "merchantid", required = true) Long merchantid,
			@Valid @RequestBody Offer newOffer) {
		Offer offer = offerService.saveMerchantOffer(merchantid, newOffer);
		Link selfLink = linkTo(methodOn(OfferController.class).getOffer(offer.getId(), merchantid)).withSelfRel()
				.expand();
		offer.add(selfLink);

		return new ResponseEntity(offer, HttpStatus.CREATED);
	}

	/**
	* Return a list of {@link Offer} filtered by status
	 * 
	 * 
	 * @return A list with all existing offers filtered by status
	 */
	@GetMapping(path = Path.OFFERS_BY_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
	List<Offer> findOfferByStatus(@RequestParam(name = "merchantid", required = false) Long merchantid,
			@RequestParam(name = "status", required = true) String status) {

		List<Offer> offerList = offerService.findOffersByStatus(merchantid, status);
		for (final Offer offer : offerList) {
			Link selfLink = linkTo(methodOn(OfferController.class).getOffer(offer.getId(), merchantid)).withSelfRel()
					.expand();
			offer.add(selfLink);
		}
		return offerList;
	}

	/**
	 * Return an {@link Offer} object.
	 * 
	 * @return Return an {@link Offer} object
	 * 
	 * @throws MerchantNotFoundException When a merchant with the specified id does
	 *                                   not exist
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(Path.OFFERS_ID)
	ResponseEntity getOffer(@PathVariable String offerid,
			@RequestParam(name = "merchantid", required = false) Long merchantid) {
		
		Optional<Offer> offer = offerService.findOffer(merchantid, offerid);
		if (offer.isPresent()) {
			Link selfLink = linkTo(methodOn(OfferController.class).getOffer(offerid, merchantid)).withSelfRel()
					.expand();
			offer.get().add(selfLink);
		}
		return new ResponseEntity(offer, HttpStatus.OK);
	}

	/**
	 * Update an {@link Offer}
	 * 
	 * 
	 * @return Offer - The updated offer
	 * 
	 * @throws MerchantNotFoundException         When a merchant with the specified
	 *                                           id does not exist
	 * @throws OfferNotFoundException            When an offer with the specified id
	 *                                           does not exist
	 * @throws OfferPrimaryKeyViolationException When the given offerid already
	 *                                           exist
	 * @throws InvalidOfferException             When the provided offer object is
	 *                                           invalid
	 * @throws IllegalOfferStatusException       When an offer with the specified id
	 *                                           has an invalid state
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(Path.OFFERS_ID)
	ResponseEntity updateOffer(@RequestParam(name = "merchantid", required = true) Long merchantid,
			@PathVariable String offerid, @Valid @RequestBody Offer newOffer) {
		
		Offer offer = offerService.updateMerchantOffer(merchantid, offerid, newOffer);
		Link selfLink = linkTo(methodOn(OfferController.class).getOffer(offerid, merchantid)).withSelfRel().expand();
		offer.add(selfLink);
		return new ResponseEntity(offer, HttpStatus.OK);
	}

	/**
	 * 
	 * Change the status of an existing {@link Offer} to
	 * {@link OfferStatus#CANCELLED}
	 * 
	 * 
	 * @return Offer - The given offer with status = Cancelled
	 * 
	 * @throws OfferNotFoundException      When the offer does not exist
	 * @throws IllegalOfferStatusException When the offer is already expired
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping(Path.OFFERS_ID)
	ResponseEntity cancelOffer(@RequestParam(name = "merchantid", required = true) Long merchantid,
			@PathVariable String offerid) {
		
		Offer offer = offerService.cancelOffer(merchantid, offerid);
		Link selfLink = linkTo(methodOn(OfferController.class).getOffer(offerid, merchantid)).withSelfRel().expand();
		offer.add(selfLink);
		return new ResponseEntity(offer, HttpStatus.OK);
	}
}
