package com.bitespeed.fluxkart.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bitespeed.fluxkart.requests.ContactRequest;
import com.bitespeed.fluxkart.responses.ContactResponse;
import com.bitespeed.fluxkart.services.ContactService;

@RestController
public class ContactController {

	private final ContactService contactService;

	/**
	 * @param contactService
	 */
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}
	
	@PostMapping("/identify")
	public ResponseEntity<ContactResponse> identifyContact(@RequestBody ContactRequest contactRequest){
		return ResponseEntity.ok(contactService.identifyContact(contactRequest));
	}
}
