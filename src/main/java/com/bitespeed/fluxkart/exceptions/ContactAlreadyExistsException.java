package com.bitespeed.fluxkart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContactAlreadyExistsException extends RuntimeException{

	public ContactAlreadyExistsException(String message) {
		super(message);
	}
}
