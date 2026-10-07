package com.example.fruit.converter.dto.response;

public record CurrencyResponse(
        Long id,
        String code,
        String name,
        String nameRu,
        String symbol,
        Short decimalPlaces,
        Boolean active
) { }
