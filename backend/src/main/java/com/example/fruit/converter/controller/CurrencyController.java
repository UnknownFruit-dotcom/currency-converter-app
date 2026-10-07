package com.example.fruit.converter.controller;

import com.example.fruit.converter.dto.request.CurrencyCreationRequest;
import com.example.fruit.converter.dto.request.CurrencyUpdateRequest;
import com.example.fruit.converter.dto.response.CurrencyResponse;
import com.example.fruit.converter.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping
    public ResponseEntity<CurrencyResponse> addCurrency(@Valid @RequestBody CurrencyCreationRequest request) {
        return ResponseEntity.ok(currencyService.addCurrency(request));
    }

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyResponse> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<CurrencyResponse> toggleCurrencyStatus(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.toggleCurrencyStatus(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CurrencyResponse> updateCurrency(
            @PathVariable Long id,
            @Valid @RequestBody CurrencyUpdateRequest request) {
        return ResponseEntity.ok(currencyService.updateCurrency(id, request));
    }
}
