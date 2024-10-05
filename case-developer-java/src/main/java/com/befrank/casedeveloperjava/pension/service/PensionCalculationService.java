package com.befrank.casedeveloperjava.pension.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PensionCalculationService {

    /**
     * Calculates the annual premium a participant pays as follows:
     * annual premium = (salary - franchise) * parttime percentage * premium percentage
     *
     * @param salary                the fulltime salary of participant
     * @param franchise             ...
     * @param partTimePercentage    ...
     * @param premiumPercentage     ...
     */
    public Double calculateAnnualPremium(double salary, double franchise, double partTimePercentage, double premiumPercentage) {
        BigDecimal salaryBD = BigDecimal.valueOf(salary);
        BigDecimal franchiseBD = BigDecimal.valueOf(franchise);
        BigDecimal partTimePercentageBD = BigDecimal.valueOf(partTimePercentage);
        BigDecimal premiumPercentageBD = BigDecimal.valueOf(premiumPercentage);

        BigDecimal annualPremium = salaryBD
                .subtract(franchiseBD)
                .multiply(partTimePercentageBD)
                .multiply(premiumPercentageBD);

        return annualPremium
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public Double calculateMonthlyPremium(double annualPremium) {
        BigDecimal annualPremiumBD = BigDecimal.valueOf(annualPremium);
        BigDecimal monthlyPremiumBD = annualPremiumBD.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        return monthlyPremiumBD
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // moet eigenlijk een obj returnen met datum, waarde en valuta ipv een double
    public Double calculateExpectedPensionValue(double currentPensionValue, double annualPremium, int currentAge, int retirementAge) {
        BigDecimal expectedValue = BigDecimal.valueOf(currentPensionValue);
        BigDecimal annualPremiumBD = BigDecimal.valueOf(annualPremium);
        BigDecimal yield = BigDecimal.valueOf(0.03);

        for (int year = currentAge; year < retirementAge; year++) {
            BigDecimal halfPremium = annualPremiumBD.divide(new BigDecimal(2), 2, RoundingMode.HALF_UP);
            BigDecimal interest = expectedValue
                    .add(halfPremium)
                    .multiply(yield);

            expectedValue = expectedValue
                    .add(annualPremiumBD)
                    .add(interest);
        }

        return expectedValue
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
