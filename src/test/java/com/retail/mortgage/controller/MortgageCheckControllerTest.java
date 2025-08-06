package com.retail.mortgage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.mortgage.constants.MortgageConstants;
import com.retail.mortgage.model.MortgageCheckRequest;
import com.retail.mortgage.model.MortgageCheckResponse;
import com.retail.mortgage.service.MortgageCheckService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageCheckController.class)
class MortgageCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MortgageCheckService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testMortgageCheck() throws Exception {
        var request = new MortgageCheckRequest(
            new BigDecimal("60000"),
            20,
            new BigDecimal("200000"),
            new BigDecimal("250000")
        );
    
        var response = new MortgageCheckResponse(true, new BigDecimal("1100.50"));
        when(service.checkMortgage(any())).thenReturn(response);
    
        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCost").value(1100.50));
    }
    

    @Test
    @DisplayName("Should return 400 Bad Request for null income")
    void testMortgageCheckWhenIncomeIsNull() throws Exception {
        String requestJson = """
            {
                "income": null,
                "maturityPeriod": 20,
                "loanValue": 200000,
                "houseValue": 250000
            }
        """;

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(MortgageConstants.ERROR_VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(containsString("income")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for null loan value")
    void testMortgageCheckWhenLoanValueIsNull() throws Exception {
        String requestJson = """
            {
                "income": 60000,
                "maturityPeriod": 20,
                "loanValue": null,
                "houseValue": 250000
            }
        """;

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(MortgageConstants.ERROR_VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(containsString("loanValue")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for null house value")
    void testMortgageCheckWhenHomeValueIsNull() throws Exception {
        String requestJson = """
            {
                "income": 60000,
                "maturityPeriod": 20,
                "loanValue": 200000,
                "houseValue": null
            }
        """;

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(MortgageConstants.ERROR_VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(containsString("houseValue")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for null maturity period")
    void testMortgageCheckWhenMaturityIsNull() throws Exception {
        String requestJson = """
            {
                "income": 60000,
                "maturityPeriod": null,
                "loanValue": 200000,
                "houseValue": 250000
            }
        """;

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(MortgageConstants.ERROR_VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(containsString("maturityPeriod")));
    }
}
