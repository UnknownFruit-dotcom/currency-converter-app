package com.example.fruit.converter.unit.service;

import com.example.fruit.converter.model.Currency;
import com.example.fruit.converter.model.ExchangeRate;
import com.example.fruit.converter.repository.CurrencyRepository;
import com.example.fruit.converter.repository.ExchangeRateRepository;
import com.example.fruit.converter.service.ConversionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConversionServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ConversionService service;

    private Currency rub(Long id) {
        return new Currency(id, "RUB", "Russian Ruble", "Российский рубль", "₽", (short) 2, true);
    }

    private Currency usd(Long id) {
        return new Currency(id, "USD", "United States Dollar", "Американский доллар", "$", (short) 2, true);
    }

    private Currency eur(Long id) {
        return new Currency(id, "EUR", "Euro", "Евро", "€", (short) 2, true);
    }

    @Test
    void convert_fromRubToUsd_shouldReturnCorrectAmount() {
        var rubCurrency = rub(1L);

        var foreignCurrency = usd(2L);

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(rubCurrency));

        when(currencyRepository.findById(2L)).thenReturn(Optional.of(foreignCurrency));

        var rate = new BigDecimal("86.9963");
        when(exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(eq(1L), eq(2L), any(Instant.class)))
                .thenReturn(Optional.of(new ExchangeRate(
                        1L,
                        rubCurrency,
                        foreignCurrency,
                        rate,
                        Instant.now().minus(2, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS)
                )));

        var amount = new BigDecimal("100");
        var converted = service.convert(1L, 2L, amount);

        assertEquals(new BigDecimal("1.15"), converted);
    }

    @Test
    void convert_fromUsdToRub_shouldReturnCorrectAmount() {
        var rubCurrency = rub(1L);

        var foreignCurrency = usd(2L);

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(rubCurrency));

        when(currencyRepository.findById(2L)).thenReturn(Optional.of(foreignCurrency));

        var rate = new BigDecimal("86.9963");
        when(exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(eq(1L), eq(2L), any(Instant.class)))
                .thenReturn(Optional.of(new ExchangeRate(
                        1L,
                        rubCurrency,
                        foreignCurrency,
                        rate,
                        Instant.now().minus(2, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS)
                )));

        var amount = new BigDecimal("100");
        var converted = service.convert(2L, 1L, amount);

        assertEquals(new BigDecimal("8699.63"), converted);
    }

    @Test
    void convert_fromUsdToEur_shouldReturnCorrectAmount() {
        var eurCurrency = eur(1L);

        var usdCurrency = usd(2L);

        var rubCurrency = rub(3L);

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(eurCurrency));

        when(currencyRepository.findById(2L)).thenReturn(Optional.of(usdCurrency));

        when(currencyRepository.findByCode("RUB")).thenReturn(Optional.of(rubCurrency));

        var rubToUsdRate = new BigDecimal("86.9963");
        when(exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(eq(3L), eq(2L), any(Instant.class)))
                .thenReturn(Optional.of(new ExchangeRate(
                        1L,
                        rubCurrency,
                        usdCurrency,
                        rubToUsdRate,
                        Instant.now().minus(2, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS)
                )));

        var rubToEurRate = new BigDecimal("94.35");
        when(exchangeRateRepository.findFirstByBase_IdAndTarget_IdAndExpiresAtAfterOrderByFetchedAtDesc(eq(3L), eq(1L), any(Instant.class)))
                .thenReturn(Optional.of(new ExchangeRate(
                        1L,
                        rubCurrency,
                        eurCurrency,
                        rubToEurRate,
                        Instant.now().minus(2, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS)
                )));

        var amount = new BigDecimal("100");
        var converted = service.convert(2L, 1L, amount);

        assertEquals(new BigDecimal("92.21"), converted);
    }

    @Test
    void convert_fromRubToRub_shouldReturnSameAmount() {
        var rubCurrency = rub(1L);

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(rubCurrency));

        var amount = new BigDecimal("100");
        var converted = service.convert(1L, 1L, amount);

        assertEquals(new BigDecimal("100.00"), converted);
    }
}
