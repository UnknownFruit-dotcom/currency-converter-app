package com.example.fruit.converter.dto.response;

public record CurrencyShortResponse(
        Long id,
        String code,
        String nameRu,
        String symbol,
        Short decimalPlaces
) {}
