package com.example.fruit.converter.dto.request;

import jakarta.validation.constraints.*;

public record CurrencyUpdateRequest(
        @Size(min = 3, max = 3)
        @Pattern(regexp = "^[A-Z]{3}$", message = "Code must be 3 uppercase letters")
        String code,

        @Size(max = 255)
        String name,

        @Size(max = 255)
        String nameRu,

        @Size(max = 10)
        String symbol,

        @Min(0)
        @Max(8)
        Short decimalPlaces
) { }
