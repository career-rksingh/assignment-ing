package com.retail.mortgage.repository;

import com.retail.mortgage.model.MortgageRate;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MortgageInterestRateRepository extends JpaRepository<MortgageRate, Long> {
    List<MortgageRate> findByMaturityPeriod(int maturityPeriod, Sort sort);
}
