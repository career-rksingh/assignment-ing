package com.retail.mortgage.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.retail.mortgage.constants.MortgageConstants;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class MortgageAppExceptionHandler {

    @ExceptionHandler(MortgageInterestRateNotFoundException.class)
    public ResponseEntity<Map<String, Object>> processMortageInterestRateNotFound(MortgageInterestRateNotFoundException ex, HttpServletRequest request) {
        
        return publishErrorResponse(HttpStatus.NOT_FOUND, MortgageConstants.ERROR_MORTGAGE_RATE_NOT_FOUND, ex.getMessage(), request);
    
    }

    @ExceptionHandler(MortgageRulesCheckException.class)
    public ResponseEntity<Map<String, Object>> processMortgageRules(MortgageRulesCheckException ex, HttpServletRequest request) {
       
        return publishErrorResponse(HttpStatus.BAD_REQUEST, MortgageConstants.ERROR_MORTGAGE_RULE_VALIDATION, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> processTechnicalErrors(Exception ex, HttpServletRequest request) {
        
        return publishErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, MortgageConstants.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> processValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var fieldError = ex.getBindingResult().getFieldError();
       
        String message = fieldError != null 
            ? fieldError.getField() + ": " + fieldError.getDefaultMessage() 
            : MortgageConstants.ERROR_VALIDATION_FAILED;

        
            return publishErrorResponse(HttpStatus.BAD_REQUEST, MortgageConstants.ERROR_VALIDATION_FAILED, message, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> processIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        
        return publishErrorResponse(HttpStatus.BAD_REQUEST, MortgageConstants.ERROR_BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> processTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Parameter '%s' has invalid value '%s'. Expected type: %s", 
                ex.getName(), ex.getValue());

        
                return publishErrorResponse(HttpStatus.BAD_REQUEST, MortgageConstants.ERROR_INVALID_PARAMETER, message, request);
    }

    private ResponseEntity<Map<String, Object>> publishErrorResponse(HttpStatus status, String error, String message, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getRequestURI());

        
        return new ResponseEntity<>(body, status);
    }
}
