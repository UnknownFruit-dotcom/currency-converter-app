package com.example.fruit.converter.dto.request;

import jakarta.validation.constraints.*;

public record CurrencyCreationRequest(
        @NotBlank
        @Size(min = 3, max = 3)
        @Pattern(regexp = "^[A-Z]{3}$", message = "Code must be 3 uppercase letters")
        String code,

        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String nameRu,

        @NotBlank
        @Size(max = 10)
        String symbol,

        @NotNull
        @Min(0)
        @Max(8)
        Short decimalPlaces,

        @NotNull
        Boolean active
) { }
