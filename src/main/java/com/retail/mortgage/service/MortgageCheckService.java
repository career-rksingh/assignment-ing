package com.retail.mortgage.service;

import com.retail.mortgage.constants.MortgageConstants;
import com.retail.mortgage.exception.MortgageInterestRateNotFoundException;
import com.retail.mortgage.exception.MortgageRulesCheckException;
import com.retail.mortgage.model.MortgageCheckRequest;
import com.retail.mortgage.model.MortgageCheckResponse;
import com.retail.mortgage.model.MortgageRate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class MortgageCheckService {

    private static final Logger log = LoggerFactory.getLogger(MortgageCheckService.class);


    private static final BigDecimal FOUR = BigDecimal.valueOf(4);
    private static final BigDecimal TWELVE = BigDecimal.valueOf(12);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final MathContext MATH_CONTEXT = new MathContext(SCALE, ROUNDING_MODE);

    private final MortgageInterestRateService mortgageInterestRateService;

    public MortgageCheckService(MortgageInterestRateService mortgageInterestRateService) {
        this.mortgageInterestRateService = Objects.requireNonNull(mortgageInterestRateService, "MortgageInterestRateService cannot be null");
    }

    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {
        validateMortgageRules(request);

        MortgageRate rate = mortgageInterestRateService
                .getRateByMaturity(request.maturityPeriod())
                .orElseThrow(() -> new MortgageInterestRateNotFoundException(request.maturityPeriod()));

        BigDecimal interestRate = rate.getInterestRate();

        BigDecimal monthlyCost = calculateMonthlyCost(
                request.loanValue(),
                interestRate,
                request.maturityPeriod() * 12
        );
        log.info("Monthly payment: " + monthlyCost);
        log.info("Income threshold: " + request.income().multiply(new BigDecimal("0.4")));


        return new MortgageCheckResponse(true, monthlyCost);
    }

    private void validateMortgageRules(MortgageCheckRequest request) {
        BigDecimal maxMortgage = request.income().multiply(FOUR);

        if (request.loanValue().compareTo(maxMortgage) > 0) {
            throw new MortgageRulesCheckException(MortgageConstants.ERROR_LOAN_EXCEEDS_INCOME);
        }

        if (request.loanValue().compareTo(request.houseValue()) > 0) {
            throw new MortgageRulesCheckException(MortgageConstants.ERROR_LOAN_EXCEEDS_HOME_VALUE);
        }
    }

    private BigDecimal calculateMonthlyCost(BigDecimal loan, BigDecimal annualRatePercentage, int totalMonths) {
        // Converting percentage to decimal /  4.5% to 0.045
        BigDecimal annualRate = annualRatePercentage.divide(ONE_HUNDRED, SCALE, ROUNDING_MODE);
        BigDecimal monthlyRate = annualRate.divide(TWELVE, SCALE, ROUNDING_MODE);

        // Calculating denominator
        BigDecimal denominator = BigDecimal.ONE.subtract(
                BigDecimal.ONE.add(monthlyRate).pow(-totalMonths, MATH_CONTEXT)
        );
        

        return loan.multiply(monthlyRate).divide(denominator, 2, ROUNDING_MODE);
    }
}
