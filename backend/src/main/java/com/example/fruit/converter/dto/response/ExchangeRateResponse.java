package com.example.fruit.converter.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ExchangeRateResponse(
        Long id,
        CurrencyShortResponse base,
        CurrencyShortResponse target,
        BigDecimal rate,
        Instant fetchedAt,
        Instant expiresAt
) { }
