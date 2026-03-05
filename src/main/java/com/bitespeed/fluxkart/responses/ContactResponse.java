package com.bitespeed.fluxkart.responses;

import java.util.List;

public record ContactResponse(Long primaryContactId, List<String> emails, List<String> phoneNumbers, List<Long> secondaryContactIds) {

}
