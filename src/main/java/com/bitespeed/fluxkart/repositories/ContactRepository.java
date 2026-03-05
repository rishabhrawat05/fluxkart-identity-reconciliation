package com.bitespeed.fluxkart.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitespeed.fluxkart.entity.Contact;
import com.bitespeed.fluxkart.enums.LinkedPrecedence;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	List<Contact> findByEmailOrPhoneNumberAndLinkedPrecedence(String email, String phoneNumber, LinkedPrecedence linkedPrecedence);
	
	List<Contact> findAllByEmailOrPhoneNumber(String email, String phoneNumber);
	
	Optional<Contact> findByEmailAndPhoneNumber(String email, String phoneNumber);
	
	List<Contact> findByIdOrLinkedId(Long id, Long linkedId);
}
