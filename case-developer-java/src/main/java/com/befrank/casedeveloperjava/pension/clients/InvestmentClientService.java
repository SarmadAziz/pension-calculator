package com.befrank.casedeveloperjava.pension.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class InvestmentClientService {
    private final RestClient restClient;

    public InvestmentClientService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public InvestmentResponse fetchInvestments(String accountNumber) {
        try {
            return restClient.get()
                    .uri("/api/v1/pensionfund/{accountNumber}", accountNumber)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException e) {
            // maak custom error
            throw new RuntimeException("Failed to fetch items", e);
        }
    }
}
