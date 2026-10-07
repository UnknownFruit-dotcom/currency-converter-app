package com.example.fruit.converter.controller;

import com.example.fruit.converter.dto.response.ExchangeRateResponse;
import com.example.fruit.converter.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<List<ExchangeRateResponse>> getAllExchangeRates() {
        return ResponseEntity.ok(exchangeRateService.getAllExchangeRates());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExchangeRate(@PathVariable Long id) {
        exchangeRateService.deleteExchangeRate(id);

        return ResponseEntity.noContent().build();
    }
}
