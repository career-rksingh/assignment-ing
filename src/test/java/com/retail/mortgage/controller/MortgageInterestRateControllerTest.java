package com.retail.mortgage.controller;

import com.retail.mortgage.model.MortgageRate;
import com.retail.mortgage.service.MortgageInterestRateService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageInterestRateController.class)
class MortgageInterestRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MortgageInterestRateService service;

    @Test
    void shouldReturnSortedInterestRates() throws Exception {
        var now = LocalDateTime.now();

        List<MortgageRate> rates = List.of(
            new MortgageRate(10, new BigDecimal("0.040"), now),
            new MortgageRate(20, new BigDecimal("0.030"), now),
            new MortgageRate(30, new BigDecimal("0.050"), now)
        );

        when(service.getRates(null, "asc")).thenReturn(rates.stream()
            .sorted(Comparator.comparing(MortgageRate::getInterestRate))
            .toList());
            
        mockMvc.perform(get("/api/interest-rates")
                        .param("sort", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].interestRate", is(0.03)))
                .andExpect(jsonPath("$[1].interestRate", is(0.04)))
                .andExpect(jsonPath("$[2].interestRate", is(0.05)));
    }

    @Test
    void shouldSortByInterestRateDescending() throws Exception {
        var now = LocalDateTime.now();
    
        List<MortgageRate> rates = List.of(
            new MortgageRate(10, new BigDecimal("0.040"), now),
            new MortgageRate(20, new BigDecimal("0.030"), now),
            new MortgageRate(30, new BigDecimal("0.050"), now)
        );
    
        when(service.getRates(null, "desc")).thenReturn(
            rates.stream()
                 .sorted(Comparator.comparing(MortgageRate::getInterestRate).reversed())
                 .toList()
        );
    
        mockMvc.perform(get("/api/interest-rates")
                        .param("sort", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].interestRate", is(0.05)))
                .andExpect(jsonPath("$[1].interestRate", is(0.04)))
                .andExpect(jsonPath("$[2].interestRate", is(0.03)));
    }
    

    @Test
    void shouldDefaultToAscSortingOnInvalidSortParam() throws Exception {
        var now = LocalDateTime.now();
    
        List<MortgageRate> rates = List.of(
            new MortgageRate(10, new BigDecimal("0.040"), now),
            new MortgageRate(20, new BigDecimal("0.030"), now),
            new MortgageRate(30, new BigDecimal("0.050"), now)
        );
    
        when(service.getRates(null, "xyz")).thenReturn(
            rates.stream()
                 .sorted(Comparator.comparing(MortgageRate::getInterestRate))
                 .toList()
        );
    
        mockMvc.perform(get("/api/interest-rates")
                        .param("sort", "xyz") // invalid sort param
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].interestRate", is(0.03)))
                .andExpect(jsonPath("$[1].interestRate", is(0.04)))
                .andExpect(jsonPath("$[2].interestRate", is(0.05)));
    }
    

@Test
void shouldFilterByMaturityPeriod() throws Exception {
    var now = LocalDateTime.now();

    List<MortgageRate> rates = List.of(
        new MortgageRate(10, new BigDecimal("0.04"), now),
        new MortgageRate(20, new BigDecimal("0.03"), now)
    );

    when(service.getRates(eq(20), any()))
        .thenReturn(
            rates.stream()
                 .filter(r -> r.getMaturityPeriod() == 20)
                 .toList()
        );

    mockMvc.perform(get("/api/interest-rates")
                    .param("maturityPeriod", "20")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].maturityPeriod", is(20)));
}


    @Test
    void shouldReturnEmptyWhenMaturityPeriodNotFound() throws Exception {
        var now = LocalDateTime.now();

        List<MortgageRate> rates = List.of(
            new MortgageRate(10, new BigDecimal("0.04"), now),
            new MortgageRate(20, new BigDecimal("0.03"), now)
        );

        when(service.getRates(null, null)).thenReturn(rates);

        mockMvc.perform(get("/api/interest-rates")
                        .param("maturityPeriod", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
