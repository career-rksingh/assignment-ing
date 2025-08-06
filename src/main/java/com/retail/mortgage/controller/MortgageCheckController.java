package com.retail.mortgage.controller;

import com.retail.mortgage.model.MortgageCheckRequest;
import com.retail.mortgage.model.MortgageCheckResponse;
import com.retail.mortgage.service.MortgageCheckService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mortgage-check")
public class MortgageCheckController {

    private final MortgageCheckService service;

    public MortgageCheckController(MortgageCheckService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MortgageCheckResponse> checkMortgage(@Valid @RequestBody MortgageCheckRequest req) {

        MortgageCheckResponse response = service.checkMortgage(req);

   
        return ResponseEntity.ok(response);
   
    }
}


