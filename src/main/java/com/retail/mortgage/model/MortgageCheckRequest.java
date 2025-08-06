package com.retail.mortgage.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MortgageCheckRequest(

    @NotNull(message = "Income is required")
    @DecimalMin(value = "0.01", message = "Income must be greater than zero")
    BigDecimal income,

    @NotNull(message = "Maturity period is required")
    @Positive(message = "Maturity period must be positive")
    Integer maturityPeriod,

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "0.01", message = "Loan amount must be greater than zero")
    BigDecimal loanValue,

    @NotNull(message = "House valuation is required")
    @DecimalMin(value = "0.01", message = "House valuation must be greater than zero")
    BigDecimal houseValue

) {

    public MortgageCheckRequest(BigDecimal income, BigDecimal loanValue, BigDecimal houseValue, int maturityPeriod) {
        this(income, maturityPeriod, loanValue, houseValue);
    }
    }
