package com.retail.mortgage.integration;

import com.retail.mortgage.model.MortgageCheckRequest;
import com.retail.mortgage.model.MortgageCheckResponse;
import com.retail.mortgage.model.MortgageRate;
import com.retail.mortgage.repository.MortgageInterestRateRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MortgageCheckTest {

    private static final Logger log = LoggerFactory.getLogger(MortgageCheckTest.class);


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MortgageInterestRateRepository rateRepository;

    @BeforeEach
    void setup() {
        rateRepository.deleteAll();

        rateRepository.save(new MortgageRate(30, new BigDecimal("0.5"), LocalDateTime.now())); // 5% interest
    }

    @Test
    void shouldApproveMortgageWhenCriteriaMet() {
        MortgageCheckRequest request = new MortgageCheckRequest(
            new BigDecimal("90000"),     // income
            new BigDecimal("250000"),    // loan value
            new BigDecimal("300000"),    // house value
            30                // maturity period
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MortgageCheckRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<MortgageCheckResponse> response = restTemplate.postForEntity(
            "/api/mortgage-check",
            entity,
            MortgageCheckResponse.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().feasible()).isTrue();
    }

}
