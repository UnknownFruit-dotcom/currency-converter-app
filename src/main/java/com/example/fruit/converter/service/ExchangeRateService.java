package com.example.fruit.converter.service;

import com.example.fruit.converter.exception.ResourceNotFoundException;
import com.example.fruit.converter.model.ExchangeRate;
import com.example.fruit.converter.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public void deleteExchangeRate(Long id) {
        ExchangeRate rate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate not found with id: " + id));

        log.info("Deleting exchange rate [{} - {}] fetched at: {}", rate.getBase().getCode(), rate.getTarget().getCode(), rate.getFetchedAt());
        exchangeRateRepository.delete(rate);
    }
}
