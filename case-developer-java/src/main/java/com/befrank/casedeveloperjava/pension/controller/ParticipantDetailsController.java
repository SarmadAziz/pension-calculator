package com.befrank.casedeveloperjava.pension.controller;

import com.befrank.casedeveloperjava.pension.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pensiondetails")
@Log4j2
public class ParticipantDetailsController {
    private final ParticipantService participantService;

    public ParticipantDetailsController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/{participantId}")
    public ParticipantDetailsResponse getParticipantDetails(@PathVariable("participantId") Long participantId) {
        log.info("fetching participant: ", participantId);
        return participantService.getParticipantInfo(participantId);
    }

    // todo waarde moet in obj gewrapped wodren
    @PostMapping("/recalculate-pension")
    public double createOrder(@Valid @RequestBody ExpectedPensionValueRequest expectedPensionValueRequest) {
        return participantService.recalculateExpectedPensionValue(expectedPensionValueRequest);
    }
}
