package com.retail.mortgage.service;

import com.retail.mortgage.exception.MortgageInterestRateNotFoundException;
import com.retail.mortgage.exception.MortgageRulesCheckException;
import com.retail.mortgage.model.MortgageCheckRequest;
import com.retail.mortgage.model.MortgageCheckResponse;
import com.retail.mortgage.model.MortgageRate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MortgageCheckServiceTest {

    private MortgageInterestRateService interestRateService;
    private MortgageCheckService checkService;

    @BeforeEach
    void setUp() {
        interestRateService = mock(MortgageInterestRateService.class);
        checkService = new MortgageCheckService(interestRateService);
    }

    @Test
void shouldPassMortgageValidationAndCalculateMonthlyCost() {
    BigDecimal interestRateValue = new BigDecimal("0.04");
    MortgageRate mortgageRate = new MortgageRate(20, interestRateValue, LocalDateTime.now());

    when(interestRateService.getRateByMaturity(20))
            .thenReturn(Optional.of(mortgageRate));

    MortgageCheckRequest request = new MortgageCheckRequest(
            new BigDecimal("80000"),      // income
            20,                          // maturity period
            new BigDecimal("200000"),    // loan value
            new BigDecimal("250000")     // house value
    );

    MortgageCheckResponse response = checkService.checkMortgage(request);

    assertThat(response.feasible()).isTrue();
    assertThat(response.monthlyCost()).isNotNull();
    assertThat(response.monthlyCost()).isGreaterThan(BigDecimal.ZERO);
}
    
    @Test
    void shouldThrowExceptionWhenLoanExceeds4xIncome() {
        BigDecimal interestRateValue = new BigDecimal("0.04");
        MortgageRate mortgageRate = new MortgageRate(20, interestRateValue, LocalDateTime.now());
    
        when(interestRateService.getRateByMaturity(20))
                .thenReturn(Optional.of(mortgageRate));
    
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("100000"),      // income
                20,
                new BigDecimal("600000"),      // loan exceeds 4 * income
                new BigDecimal("1000000")
        );
    
        assertThatThrownBy(() -> checkService.checkMortgage(request))
                .isInstanceOf(MortgageRulesCheckException.class)
                .hasMessageContaining("Loan exceeds 4x income");
    }
    
    @Test
    void shouldThrowExceptionWhenLoanExceedsHomeValue() {
        BigDecimal interestRateValue = new BigDecimal("0.04");
    MortgageRate mortgageRate = new MortgageRate(20, interestRateValue, LocalDateTime.now());

    when(interestRateService.getRateByMaturity(20))
            .thenReturn(Optional.of(mortgageRate));
    
        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("500000"),
                20,
                new BigDecimal("400000"),      // loan
                new BigDecimal("200000")       // house value too low
        );
    
        assertThatThrownBy(() -> checkService.checkMortgage(request))
                .isInstanceOf(MortgageRulesCheckException.class)
                .hasMessageContaining("Loan exceeds home value");
    }
    

    @Test
    void shouldThrowExceptionWhenInterestRateNotFound() {
        when(interestRateService.getRateByMaturity(25)).thenReturn(Optional.empty());

        MortgageCheckRequest request = new MortgageCheckRequest(
                new BigDecimal("300000"),
                25,
                new BigDecimal("100000"),
                new BigDecimal("350000")
        );

        assertThatThrownBy(() -> checkService.checkMortgage(request))
                .isInstanceOf(MortgageInterestRateNotFoundException.class)
                .hasMessageContaining("25");
    }
}
