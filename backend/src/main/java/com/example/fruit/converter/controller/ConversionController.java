package com.example.fruit.converter.controller;

import com.example.fruit.converter.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST API for currency conversion.
 */
@RestController
@RequestMapping("/api/convert")
@RequiredArgsConstructor
public class ConversionController {

    private final ConversionService conversionService;

    /**
     * Converts an amount between two currencies using fresh stored rates.
     */
    @GetMapping
    public ResponseEntity<BigDecimal> convert(
            @RequestParam(name = "from") Long fromCurrency,
            @RequestParam(name = "to") Long toCurrency,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(conversionService.convert(fromCurrency, toCurrency, amount));
    }
}
