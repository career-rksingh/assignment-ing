package com.retail.mortgage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mortgage_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int maturityPeriod;

    private BigDecimal interestRate;

    private LocalDateTime timestamp;

    public MortgageRate(int maturityPeriod, BigDecimal interestRate, LocalDateTime timestamp) {
        this.maturityPeriod = maturityPeriod;
        this.interestRate = interestRate;
        this.timestamp = timestamp;
    }
}
