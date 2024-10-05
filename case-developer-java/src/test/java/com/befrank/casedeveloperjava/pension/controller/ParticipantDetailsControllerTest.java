package com.befrank.casedeveloperjava.pension.controller;

import com.befrank.casedeveloperjava.pension.models.Address;
import com.befrank.casedeveloperjava.pension.service.ParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipantDetailsController.class)
public class ParticipantDetailsControllerTest {

    @MockBean
    private ParticipantService participantService;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetParticipantDetails() throws Exception {
        // given
        Long participantId = 1L;
        ParticipantDetailsResponse mockResponse = new ParticipantDetailsResponse(
                1L,
                "John Doe",
                LocalDate.of(1964, 1, 1),
                "john.doe@example.com",
                new Address("Street", 1, "amsterdam"),
                500.0,
                10000.0,
                20000.0
        );

        // when
        when(participantService.getParticipantInfo(participantId)).thenReturn(mockResponse);

        // then
        mockMvc.perform(get("/api/v1/pensiondetails/{participantId}", participantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(participantId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(participantService, times(1)).getParticipantInfo(participantId);
    }

    @Test
    public void testRecalculatePension() throws Exception {
        // given
        ExpectedPensionValueRequest request = new ExpectedPensionValueRequest(1L, 65, 10000.0);
        double recalculatedValue = 25000.0;

        // when
        when(participantService.recalculateExpectedPensionValue(any(ExpectedPensionValueRequest.class))).thenReturn(recalculatedValue);

        // then
        mockMvc.perform(post("/api/v1/pensiondetails/recalculate-pension")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(recalculatedValue)));

        verify(participantService, times(1)).recalculateExpectedPensionValue(any(ExpectedPensionValueRequest.class));
    }
}