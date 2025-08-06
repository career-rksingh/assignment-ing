package com.retail.mortgage.exception;

import com.retail.mortgage.constants.MortgageConstants;

/**
 * Exception in case of an interest rate is not found for a given maturity period.
 */
public class MortgageInterestRateNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int maturityPeriod;

    public MortgageInterestRateNotFoundException(int maturityPeriod) {
        super(MortgageConstants.ERROR_MORTGAGE_RATE_NOT_FOUND_GIVEN_MATURITY + maturityPeriod);
        this.maturityPeriod = maturityPeriod;
    }

    public int getMaturityPeriod() {
        return maturityPeriod;
    }

    @Override
    public String toString() {
        return "MortgageInterestRateNotFoundException{" +
                "maturityPeriod=" + maturityPeriod +
                ", message=" + getMessage() +
                '}';
    }
}
