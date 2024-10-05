package com.befrank.casedeveloperjava.pension.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PensionCalculationServiceTest {

    private PensionCalculationService pensionCalculationService = new PensionCalculationService();

    @Test
    public void testCalculateAnnualPremium() {
        // given
        double salary = 60000.00;
        double franchise = 15599.00;
        double partTimePercentage = 0.80;
        double premiumPercentage = 0.05;

        // Then
        Double result = pensionCalculationService.calculateAnnualPremium(salary, franchise, partTimePercentage, premiumPercentage);
        assertEquals(1776.04, result);
    }

    @Test
    public void testCalculateMonthlyPremium() {
        // given
        double annualPremium = 1776.04;

        // then
        Double result = pensionCalculationService.calculateMonthlyPremium(annualPremium);
        assertEquals(148.0, result);
    }

    @Test
    public void testCalculateExpectedPensionValueForOneYear() {
        // given
        double currentPensionValue = 100000.00;
        double annualPremium = 1776.04;
        int currentAge = 60;
        int retirementAge = 61;

        // then
        Double result = pensionCalculationService.calculateExpectedPensionValue(currentPensionValue, annualPremium, currentAge, retirementAge);
        assertEquals(104802.68, result);
    }

    @Test
    public void testCalculateExpectedPensionValueForFiveYears() {
        // given
        double currentPensionValue = 100000.00;
        double annualPremium = 1776.04;
        int currentAge = 60;
        int retirementAge = 65;

        // then
        Double result = pensionCalculationService.calculateExpectedPensionValue(currentPensionValue, annualPremium, currentAge, retirementAge);
        assertEquals(125498.08, result);
    }
}