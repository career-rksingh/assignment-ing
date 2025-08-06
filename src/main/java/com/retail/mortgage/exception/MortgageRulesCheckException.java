package com.retail.mortgage.exception;

/**
 * Exception in case of, defined mortgage rules are not fulfilled.
 */
public class MortgageRulesCheckException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MortgageRulesCheckException(String message) {
        super(message);
    }

    public MortgageRulesCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "MortgageRulesCheckException{" +
                "message=" + getMessage() +
                '}';
    }
}
