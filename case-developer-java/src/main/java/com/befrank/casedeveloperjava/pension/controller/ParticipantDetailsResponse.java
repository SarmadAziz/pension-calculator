package com.befrank.casedeveloperjava.pension.controller;

import com.befrank.casedeveloperjava.pension.models.Address;

import java.time.LocalDate;

// todo eigenlijk moet currentPensionValue de lijst van investements bevatten ipv alleen het totaal bedrag
public record ParticipantDetailsResponse(
        Long id,
        String name,
        LocalDate dateOfBirth,
        String email,
        Address address,
        Double monthlyPremium,
        Double currentPensionValue,
        Double expectedPensionValue) {
}