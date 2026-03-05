package com.bitespeed.fluxkart.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.bitespeed.fluxkart.entity.Contact;
import com.bitespeed.fluxkart.enums.LinkedPrecedence;
import com.bitespeed.fluxkart.repositories.ContactRepository;
import com.bitespeed.fluxkart.requests.ContactRequest;
import com.bitespeed.fluxkart.responses.ContactResponse;

@Service
public class ContactService {

	private final ContactRepository contactRepository;

	public ContactService(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	public ContactResponse identifyContact(ContactRequest request) {

		// first we have to get all contacts by email or phone number
		List<Contact> contacts = contactRepository.findAllByEmailOrPhoneNumber(request.email(), request.phoneNumber());

		// no contacts exists, so we create a new contact
		if (contacts.isEmpty()) {

			Contact contact = new Contact();
			contact.setEmail(request.email());
			contact.setPhoneNumber(request.phoneNumber());
			contact.setLinkedPrecedence(LinkedPrecedence.PRIMARY);
			contact.setCreatedAt(LocalDateTime.now());
			contact.setUpdatedAt(LocalDateTime.now());

			contactRepository.save(contact);

			return toContactResponse(contact);
		}

		// Now we need to find all contacts which are primary from above fetched
		// contacts
		List<Contact> primaryContacts = contacts.stream()
				.filter(c -> c.getLinkedPrecedence() == LinkedPrecedence.PRIMARY).toList();

		Contact primaryContact;

		// Now we need to get the oldest Primary as to make a singular primary and rest
		// secondary
		if (!primaryContacts.isEmpty()) {

			primaryContact = primaryContacts.stream().min(Comparator.comparing(Contact::getCreatedAt)).get();

		}

		else {
			// Only secondary contacts found then fetch primary using linkedId
			Long primaryId = contacts.get(0).getLinkedId();
			primaryContact = contactRepository.findById(primaryId).orElseThrow();
		}

		// converting rest to secondary
		if (primaryContacts.size() > 1) {

			for (Contact cont : primaryContacts) {

				if (!cont.getId().equals(primaryContact.getId())) {

					cont.setLinkedId(primaryContact.getId());
					cont.setLinkedPrecedence(LinkedPrecedence.SECONDARY);
					cont.setUpdatedAt(LocalDateTime.now());

					// saving to db
					contactRepository.save(cont);
				}
			}
		}

		// if request contain new email than already have contacts
		boolean emailExists = request.email() != null
				&& contacts.stream().anyMatch(c -> request.email().equals(c.getEmail()));

		// if request contain new phoneNumber than already have contacts
		boolean phoneExists = request.phoneNumber() != null
				&& contacts.stream().anyMatch(c -> request.phoneNumber().equals(c.getPhoneNumber()));

		// if new email or phone number is present in request then create new contact
		if (!emailExists || !phoneExists) {

			Contact contact = new Contact();
			contact.setEmail(request.email());
			contact.setPhoneNumber(request.phoneNumber());
			contact.setLinkedPrecedence(LinkedPrecedence.SECONDARY);
			contact.setLinkedId(primaryContact.getId());
			contact.setCreatedAt(LocalDateTime.now());
			contact.setUpdatedAt(LocalDateTime.now());

			contactRepository.save(contact);

			contacts.add(contact);
		}

		List<Contact> allContacts = contactRepository.findByIdOrLinkedId(primaryContact.getId(),
				primaryContact.getId());

		// creating contact response
		ContactResponse contactResponse = new ContactResponse(primaryContact.getId(),

				new ArrayList<>(
						allContacts.stream().map(Contact::getEmail).filter(Objects::nonNull).distinct().toList()),

				new ArrayList<>(
						allContacts.stream().map(Contact::getPhoneNumber).filter(Objects::nonNull).distinct().toList()),

				new ArrayList<>(allContacts.stream().filter(c -> c.getLinkedPrecedence() == LinkedPrecedence.SECONDARY)
						.map(Contact::getId).toList()));

		return contactResponse;
	}

	public ContactResponse toContactResponse(Contact contact) {

		return new ContactResponse(contact.getId(), new ArrayList<>(List.of(contact.getEmail())),
				new ArrayList<>(List.of(contact.getPhoneNumber())), new ArrayList<>());
	}
}