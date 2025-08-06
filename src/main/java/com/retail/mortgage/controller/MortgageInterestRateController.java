package com.retail.mortgage.controller;

import com.retail.mortgage.model.MortgageRate;
import com.retail.mortgage.service.MortgageInterestRateService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interest-rates")
public class MortgageInterestRateController {

    private final MortgageInterestRateService service;

    public MortgageInterestRateController(MortgageInterestRateService service) {
       
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MortgageRate>> getRates(
            @RequestParam(required = false) Integer maturityPeriod,
            @RequestParam(defaultValue = "asc") String sort
    ) {
       
        List<MortgageRate> mortgageRates = service.getRates(maturityPeriod, sort);
       
        return ResponseEntity.ok(mortgageRates);
   
    }
}
