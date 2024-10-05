package com.befrank.casedeveloperjava.pension.service;

import com.befrank.casedeveloperjava.pension.clients.Investment;
import com.befrank.casedeveloperjava.pension.clients.InvestmentClientService;
import com.befrank.casedeveloperjava.pension.clients.InvestmentResponse;
import com.befrank.casedeveloperjava.pension.controller.ExpectedPensionValueRequest;
import com.befrank.casedeveloperjava.pension.controller.ParticipantDetailsResponse;
import com.befrank.casedeveloperjava.pension.exceptions.ParticipantNotFoundException;
import com.befrank.casedeveloperjava.pension.models.Address;
import com.befrank.casedeveloperjava.pension.models.Employment;
import com.befrank.casedeveloperjava.pension.models.Participant;
import com.befrank.casedeveloperjava.pension.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {ParticipantService.class, PensionCalculationService.class})
public class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private PensionCalculationService pensionCalculationService;

    @MockBean
    private InvestmentClientService investmentClientService;

    @MockBean
    private ParticipantRepository participantRepository;

    private Participant participant;
    private Employment employment;

    @BeforeEach
    public void setUp() {
        // given
        initParticipant();
    }

    @Test
    public void testRecalculateExpectedPensionValue() {
        // when
        ExpectedPensionValueRequest request = new ExpectedPensionValueRequest(1L, 61, 100000.00);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        // then
        Double result = participantService.recalculateExpectedPensionValue(request);
        assertEquals(104802.68, result);
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    public void testGetParticipantInfo() {
        // when
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));
        when(investmentClientService.fetchInvestments("1234567890"))
                .thenReturn(new InvestmentResponse(List.of(
                        new Investment("Fund1", 60000.00),
                        new Investment("Fund2", 40000.00)
                )));

        // then
        ParticipantDetailsResponse response = participantService.getParticipantInfo(1L);
        assertEquals(1L, response.id());
        assertEquals("John Doe", response.name());
        assertEquals(LocalDate.of(1964, 1, 1), response.dateOfBirth());
        assertEquals("john.doe@example.com", response.email());
        assertEquals(148.0, response.monthlyPremium());
        assertEquals(100000.00, response.currentPensionValue());
        assertEquals(125498.08, response.expectedPensionValue());
    }

    @Test
    public void testGetParticipantInfo_ThrowsParticipantNotFoundException() {
        // when
        when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(ParticipantNotFoundException.class, () -> {
            participantService.getParticipantInfo(1L);
        });
        verify(participantRepository, times(1)).findById(1L);
    }

    private void initParticipant() {
        employment = new Employment();
        employment.setFullTimeSalary(60000.00);
        employment.setFranchise(15599.00);
        employment.setPartTimePercentage(0.8);
        employment.setAvailablePremiumPercentage(0.05);

        participant = new Participant();
        participant.setId(1L);
        participant.setName("John Doe");
        participant.setDateOfBirth(LocalDate.of(1964, 1, 1));
        participant.setEmail("john.doe@example.com");
        participant.setAddress(new Address("123 Main St", 1, "zaandam"));
        participant.setEmployment(employment);
        participant.setRetirementAccount("1234567890");
        participant.setRetirementAge(65);
    }
}