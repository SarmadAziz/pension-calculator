package com.befrank.casedeveloperjava.pension.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ParticipantDetailsIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testGetParticipantDetails() {
        Long knownParticipantId = 1L;
        given()
            .port(port)
        .when()
            .get("/api/v1/pensiondetails/{participantId}", knownParticipantId)
        .then()
            .statusCode(200)
            .body("id", equalTo(knownParticipantId.intValue()))
            .body("name", equalTo("bob"))
            .body("dateOfBirth", equalTo("1964-01-01"))
            .body("email", equalTo("myEmail@live.com"));
    }
}