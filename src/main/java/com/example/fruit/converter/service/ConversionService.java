package com.example.fruit.converter.service;

import com.example.fruit.converter.exception.ResourceNotFoundException;
import com.example.fruit.converter.model.Currency;
import com.example.fruit.converter.model.ExchangeRate;
import com.example.fruit.converter.repository.CurrencyRepository;
import com.example.fruit.converter.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

import static java.math.RoundingMode.HALF_UP;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversionService {

    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public BigDecimal convert(Long fromId, Long toId, BigDecimal amount) {
        Currency fromCurrency = currencyRepository.findById(fromId)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + fromId));

        Currency toCurrency = currencyRepository.findById(toId)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + toId));

        if (fromId.compareTo(toId) == 0)
            return amount.setScale(toCurrency.getDecimalPlaces(), HALF_UP);
        else if (fromCurrency.getCode().equals("RUB") || toCurrency.getCode().equals("RUB")) {
            Long rubId = fromCurrency.getCode().equals("RUB") ? fromId : toId;
            Long foreignId = rubId.compareTo(fromCurrency.getId()) == 0 ? toId : fromId;

            ExchangeRate rate = exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(
                    rubId,
                    foreignId,
                    Instant.now()
            ).orElseThrow(() -> {
                log.error("Fresh rate not found for currency id={}", fromId);
                return new ResourceNotFoundException("Fresh rate not found for currency id=" + fromId);
            });

            if (fromCurrency.getCode().equals("RUB")) {
                BigDecimal inverted = BigDecimal.ONE.divide(
                        rate.getRate(),
                        8,
                        HALF_UP
                );

                return inverted.multiply(amount).setScale(toCurrency.getDecimalPlaces(), HALF_UP);
            } else {
                return amount.multiply(rate.getRate()).setScale(toCurrency.getDecimalPlaces(), HALF_UP);
            }
        }
        else {
            Currency baseCurrency = currencyRepository.findByCode("RUB")
                    .orElseThrow(() -> new ResourceNotFoundException("Base Currency not found in database"));
            log.error("Base Currency not found in database");

            ExchangeRate rate = exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(
                    baseCurrency.getId(),
                    fromId,
                    Instant.now()
            ).orElseThrow(() -> {
                log.error("Fresh rate not found for currency id={}", fromId);
                return new ResourceNotFoundException("Fresh rate not found for currency id=" + fromId);
            });

            BigDecimal fromInRub = amount.multiply(rate.getRate());

            ExchangeRate finalRate = exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(
                    baseCurrency.getId(),
                    toId,
                    Instant.now()
            ).orElseThrow(() -> {
                log.error("Fresh rate not found for currency id={}", fromId);
                return new ResourceNotFoundException("Fresh rate not found for currency id=" + fromId);
            });

            BigDecimal invertedFinal = BigDecimal.ONE.divide(
                    finalRate.getRate(),
                    8,
                    HALF_UP
            );

            return invertedFinal.multiply(fromInRub).setScale(toCurrency.getDecimalPlaces(), HALF_UP);
        }
    }
}
