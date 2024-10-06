package com.befrank.casedeveloperjava.pension.controller;

import com.befrank.casedeveloperjava.pension.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/v1/pensiondetails")
@Log4j2
public class ParticipantDetailsController {
    private final ParticipantService participantService;

    public ParticipantDetailsController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @Operation(summary = "Get participant details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "found participant",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ParticipantDetailsResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "participnat not found", content = @Content)
    })
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
