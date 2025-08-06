package com.retail.mortgage.service;

import com.retail.mortgage.model.MortgageRate;
import com.retail.mortgage.repository.MortgageInterestRateRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MortgageInterestRateServiceTest {

    private MortgageInterestRateService service;
    private MortgageInterestRateRepository mortgageInterestRateRepository;

    @BeforeEach
    void setUp() {
        mortgageInterestRateRepository = mock(MortgageInterestRateRepository.class);
        service = new MortgageInterestRateService(mortgageInterestRateRepository);

        var now = LocalDateTime.now();
        List<MortgageRate> mockRates = List.of(
            new MortgageRate(5, new BigDecimal("3.50"), now),
            new MortgageRate(10, new BigDecimal("3.75"), now),
            new MortgageRate(15, new BigDecimal("4.00"), now),
            new MortgageRate(20, new BigDecimal("4.10"), now),
            new MortgageRate(25, new BigDecimal("4.25"), now),
            new MortgageRate(30, new BigDecimal("4.50"), now)
        );

        // Mock findAll(Sort)
        when(mortgageInterestRateRepository.findAll(org.mockito.ArgumentMatchers.<org.springframework.data.domain.Sort>any()))
    .thenReturn(mockRates);


        // Mock findByMaturityPeriod(int, Sort)
        when(mortgageInterestRateRepository.findByMaturityPeriod(20, null))
                .thenReturn(List.of(mockRates.get(3)));

        when(mortgageInterestRateRepository.findByMaturityPeriod(12, null))
                .thenReturn(List.of());  // empty list for no match
    }

    @Test
    void shouldReturnAllMortgageRates() {
        List<MortgageRate> rates = service.getRates(null, null);

        assertThat(rates).isNotNull();
        assertThat(rates).hasSize(6);
        assertThat(rates)
            .extracting(MortgageRate::getMaturityPeriod)
            .containsExactly(5, 10, 15, 20, 25, 30);
    }

    @Test
    void shouldReturnInterestRateForExistingMaturityPeriod() {
        Optional<MortgageRate> rateOpt = service.getRateByMaturity(20);

        assertThat(rateOpt).isPresent();
        assertThat(rateOpt.get().getInterestRate()).isEqualByComparingTo("4.10");
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistingMaturityPeriod() {
        Optional<MortgageRate> rateOpt = service.getRateByMaturity(12);

        assertThat(rateOpt).isNotPresent();
    }
}
