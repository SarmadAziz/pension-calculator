package com.befrank.casedeveloperjava.pension.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    private final Validator validator;

    public ParticipantTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidParticipant() {
        Participant participant = Participant.builder()
                .name("John Doe")
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .email("john.doe@example.com")
                .retirementAccount("1234567890")
                .isEmployed(true)
                .retirementAge(65)
                .build();

        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);
        assertTrue(violations.isEmpty(), "There should be no validation violations");
    }

    @Test
    public void testInvalidParticipant() {
        Participant participant = Participant.builder()
                .name("")
                .dateOfBirth(LocalDate.of(2999, 1, 1))
                .email("invalid-email")
                .retirementAccount("")
                .isEmployed(true)
                .retirementAge(150)
                .build();

        Set<ConstraintViolation<Participant>> violations = validator.validate(participant);

        assertThat(violations).hasSize(5);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "Email onjuist",
                        "rekening is verplicht",
                        "Pensioenleeftijd moet onder 67",
                        "naam verplicht",
                        "GEboortedatum moet in het verleden"
                );
    }
}