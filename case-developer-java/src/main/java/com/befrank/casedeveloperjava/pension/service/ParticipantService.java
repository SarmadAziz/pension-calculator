package com.befrank.casedeveloperjava.pension.service;

import com.befrank.casedeveloperjava.pension.clients.Investment;
import com.befrank.casedeveloperjava.pension.clients.InvestmentClientService;
import com.befrank.casedeveloperjava.pension.controller.ExpectedPensionValueRequest;
import com.befrank.casedeveloperjava.pension.controller.ParticipantDetailsResponse;
import com.befrank.casedeveloperjava.pension.exceptions.ParticipantNotFoundException;
import com.befrank.casedeveloperjava.pension.models.Participant;
import com.befrank.casedeveloperjava.pension.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ParticipantService {
    private final InvestmentClientService investmentClientService;

    private final ParticipantRepository participantRepository;

    private final PensionCalculationService pensionCalculationService;

    public ParticipantService(InvestmentClientService investmentClientService, ParticipantRepository participantRepository, PensionCalculationService pensionCalculationService) {
        this.investmentClientService = investmentClientService;
        this.participantRepository = participantRepository;
        this.pensionCalculationService = pensionCalculationService;
    }

    public Double recalculateExpectedPensionValue(ExpectedPensionValueRequest expectedPensionValueRequest){
        var participant = updateRetirementAge(expectedPensionValueRequest);
        var currentPensionValue = expectedPensionValueRequest.currentPensionValue();
        var annualPremium = getAnnualPremium(participant);
        return getExpectedPensionValue(participant, currentPensionValue, annualPremium);
    }

    public ParticipantDetailsResponse getParticipantInfo(Long participantId){
        var participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found"));

        var currentPensionValue = getCurrentPensionValue(participant.getRetirementAccount());
        var annualPremium = getAnnualPremium(participant);
        var expectedPensionValue = getExpectedPensionValue(participant, currentPensionValue, annualPremium);

        return new ParticipantDetailsResponse(participant.getId(),
                participant.getName(),
                participant.getDateOfBirth(),
                participant.getEmail(),
                participant.getAddress(),
                pensionCalculationService.calculateMonthlyPremium(annualPremium),
                currentPensionValue,
                expectedPensionValue
                );
    }

    private Participant updateRetirementAge(ExpectedPensionValueRequest expectedPensionValueRequest) {
        var participant = participantRepository.findById(expectedPensionValueRequest.participantId())
                .orElseThrow(() -> new ParticipantNotFoundException("Participant not found"));
        participant.setRetirementAge(expectedPensionValueRequest.updatedRetirementAge());
        participantRepository.save(participant);
        return participant;
    }

    private Double getExpectedPensionValue(Participant participant, Double currentPensionValue, Double annualPremium) {
        var currentAge = Period.between(participant.getDateOfBirth(), LocalDate.now()).getYears();
        var expectedPensionValue = pensionCalculationService.calculateExpectedPensionValue(
                currentPensionValue,
                annualPremium,
                currentAge,
                participant.getRetirementAge()
        );
        return expectedPensionValue;
    }

    private Double getAnnualPremium(Participant participant) {
        var employment = participant.getEmployment();
        var annualPremium = pensionCalculationService.calculateAnnualPremium(
                employment.getFullTimeSalary(),
                employment.getFranchise(),
                employment.getPartTimePercentage(),
                employment.getAvailablePremiumPercentage()
        );
        return annualPremium;
    }

    private Double getCurrentPensionValue(String accountNumber) {
        var investmentResponse = investmentClientService.fetchInvestments(accountNumber);
        return investmentResponse.investments().stream().mapToDouble(Investment::amount).sum();
    }
}
