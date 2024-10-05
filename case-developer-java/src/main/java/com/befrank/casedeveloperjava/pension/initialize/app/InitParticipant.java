package com.befrank.casedeveloperjava.pension.initialize.app;

import com.befrank.casedeveloperjava.pension.models.Address;
import com.befrank.casedeveloperjava.pension.models.Employment;
import com.befrank.casedeveloperjava.pension.models.Participant;
import com.befrank.casedeveloperjava.pension.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class InitParticipant {
    private final ParticipantRepository participantRepository;

    public InitParticipant(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @PostConstruct
    public void init() {
        initParticipant();
    }

    private void initParticipant() {
        Address address = Address.builder()
                .street("myStreet")
                .number(1)
                .city("amsterdam")
                .build();

        Employment employment = Employment.builder()
                .fullTimeSalary(60000.0)
                .partTimePercentage(0.8)
                .availablePremiumPercentage(0.05)
                .franchise(15599.0)
                .build();

        Participant participant = Participant.builder()
                .name("bob")
                .employment(employment)
                .address(address)
                .email("myEmail@live.com")
                .dateOfBirth(LocalDate.of(1964, 1,1))
                .isEmployed(true)
                .retirementAge(67)
                .retirementAccount("INGB00012345678")
                .build();

        participantRepository.save(participant);
    }

}
