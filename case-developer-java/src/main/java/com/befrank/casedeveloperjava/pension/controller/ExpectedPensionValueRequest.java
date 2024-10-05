package com.befrank.casedeveloperjava.pension.controller;

public record ExpectedPensionValueRequest(
        Long participantId,
        Integer updatedRetirementAge,
        Double currentPensionValue
) {
}
