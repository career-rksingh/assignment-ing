package com.retail.mortgage.model;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record MortgageCheckResponse(

    boolean feasible,

    @NotNull(message = "Monthly emi must be provided")
    @DecimalMin(value = "0.00", message = "Monthly emi cannot be negative")
    BigDecimal monthlyCost

) {}


