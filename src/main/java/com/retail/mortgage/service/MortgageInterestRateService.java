package com.retail.mortgage.service;

import com.retail.mortgage.model.MortgageRate;
import com.retail.mortgage.repository.MortgageInterestRateRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MortgageInterestRateService {

    private final MortgageInterestRateRepository repository;

    public MortgageInterestRateService(MortgageInterestRateRepository repository) {
        this.repository = repository;
    }

    public List<MortgageRate> getRates(Integer maturityPeriod, String sortDirection) {
        Sort sort = Sort.by("interestRate");
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        if (maturityPeriod != null) {
            return repository.findByMaturityPeriod(maturityPeriod, sort);
        } else {
            return repository.findAll(sort);
        }
    }
    public Optional<MortgageRate> getRateByMaturity(int years) {
        return repository.findByMaturityPeriod(years, null).stream().findFirst();
    }
    
}
