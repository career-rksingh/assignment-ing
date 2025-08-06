package com.retail.mortgage.constants;

public final class MortgageConstants {

    private MortgageConstants() {
    }

    public static final String ERROR_LOAN_EXCEEDS_INCOME = "Loan exceeds 4x income.";
    public static final String ERROR_LOAN_EXCEEDS_HOME_VALUE = "Loan exceeds home value.";
    public static final String ERROR_INTEREST_RATE_NOT_FOUND = "No interest rate found for maturity period: ";

    public static final String ERROR_VALIDATION_FAILED = "Validation failed";
    public static final String ERROR_BAD_REQUEST = "Bad Request";
    public static final String ERROR_INVALID_PARAMETER = "Invalid parameter type";
    public static final String ERROR_INTERNAL = "Internal error";
    public static final String INTERNAL_SERVER_ERROR="Internal Server Error";
    public static final String ERROR_MORTGAGE_RULE_VALIDATION = "Mortgage Rule validation failed";
    public static final String ERROR_MORTGAGE_RATE_NOT_FOUND = "Mortgage Interest rate not found";
    public static final String ERROR_MORTGAGE_RATE_NOT_FOUND_GIVEN_MATURITY = "No mortgage-interest rate found for a given maturity period:";

    

    public static final String DEFAULT_PROFILE = "local";
}
