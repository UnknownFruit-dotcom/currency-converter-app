package com.example.fruit.converter.controller;

import com.example.fruit.converter.dto.response.CurrencyResponse;
import com.example.fruit.converter.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyResponse> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }
}
