package com.example.fruit.converter.seeder;

import com.example.fruit.converter.dto.request.CurrencyCreationRequest;
import com.example.fruit.converter.repository.CurrencyRepository;
import com.example.fruit.converter.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final CurrencyRepository currencyRepository;
    private final CurrencyService currencyService;

    @Override
    public void run(String... args) {
        log.info("Database seeder running...");

        seedIfAbsent(
            "RUB",
            "Russian Ruble",
            "Российский рубль",
            "₽",
                (short)2,
            true);

        seedIfAbsent(
            "USD",
            "United States Dollar",
            "Американский доллар",
            "$",
                (short)2,
            true);

        seedIfAbsent(
            "EUR",
            "Euro",
            "Евро",
            "€",
                (short)2,
            true);
    }

    private void seedIfAbsent(String code, String name, String nameRu, String symbol, Short decimalPlaces, Boolean active) {
        if (!currencyRepository.existsByCode(code)) {
            currencyService.addCurrency(new CurrencyCreationRequest(
                    code,
                    name,
                    nameRu,
                    symbol,
                    decimalPlaces,
                    active));

            log.info("Seeded currency {}", code);
        }
    }
}
