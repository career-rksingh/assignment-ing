package com.retail.mortgage.integration;

import com.retail.mortgage.model.MortgageRate;
import com.retail.mortgage.repository.MortgageInterestRateRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MortgageInterestRateIntegrationTest {

    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MortgageInterestRateRepository rateRepository;

    private String apiUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void setupTestData() {
        rateRepository.deleteAll(); // clean DB to ensure consistency

        rateRepository.saveAll(List.of(
            new MortgageRate(5, new BigDecimal("0.025"), LocalDateTime.now()),
            new MortgageRate(5, new BigDecimal("0.030"), LocalDateTime.now().minusDays(1)),
            new MortgageRate(10, new BigDecimal("0.035"), LocalDateTime.now())
        ));
    }

    @Test
    void shouldReturnAllInterestRates() {
        ResponseEntity<List<MortgageRate>> response = restTemplate.exchange(
            apiUrl("/api/interest-rates"),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<MortgageRate> rates = response.getBody();
        assertThat(rates).isNotNull();
        assertThat(rates).isNotEmpty();
    }

    @Test
    void shouldFilterAndSortInterestRatesDescending() {
        String uri = apiUrl("/api/interest-rates?maturityPeriod=5&sort=desc");

        ResponseEntity<List<MortgageRate>> response = restTemplate.exchange(
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<MortgageRate> rates = response.getBody();
        assertThat(rates).isNotNull();
        assertThat(rates).isNotEmpty();

        MortgageRate rate = rates.get(0);
        assertThat(rate.getMaturityPeriod()).isEqualTo(5);
        assertThat(rate.getInterestRate()).isNotNull();
    }
}
