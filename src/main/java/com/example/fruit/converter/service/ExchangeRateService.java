package com.example.fruit.converter.service;

import com.example.fruit.converter.dto.response.ExchangeRateResponse;
import com.example.fruit.converter.exception.ResourceNotFoundException;
import com.example.fruit.converter.mapper.ExchangeRateMapper;
import com.example.fruit.converter.model.Currency;
import com.example.fruit.converter.model.ExchangeRate;
import com.example.fruit.converter.repository.CurrencyRepository;
import com.example.fruit.converter.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeRateService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    public List<ExchangeRateResponse> getAllExchangeRates() { return exchangeRateMapper.toResponseList(exchangeRateRepository.findAll()); }

    @Transactional
    public void deleteExchangeRate(Long id) {
        ExchangeRate rate = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange rate not found with id: " + id));

        log.info("Deleting exchange rate [{} - {}] fetched at: {}", rate.getBase().getCode(), rate.getTarget().getCode(), rate.getFetchedAt());
        exchangeRateRepository.delete(rate);
    }

    @Transactional
    public List<ExchangeRateResponse> fetchAndStoreRates() throws Exception {
        Currency baseCurrency = currencyRepository.findByCode("RUB")
                .orElseThrow(() -> new ResourceNotFoundException("Base Currency not found in database"));

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.cbr-xml-daily.ru/daily_json.js"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode nodeBody = mapper.readTree(response.body());
            var array = nodeBody.get("Valute");

            List<ExchangeRate> rates = new ArrayList<>();
            for (JsonNode node : array) {
                var targetCode = node.get("CharCode").asString();

                Currency targetCurrency = currencyRepository.findByCode(targetCode).orElse(null);
                if (targetCurrency == null) {
                    continue;
                }

                ExchangeRate rate = new ExchangeRate();
                rate.setBase(baseCurrency);
                rate.setTarget(targetCurrency);
                rate.setRate(node.get("Value").asDecimal().divide(node.get("Nominal").asDecimal()));
                rate.setFetchedAt(Instant.now());
                rate.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));

                rates.add(rate);
            }

            return exchangeRateMapper.toResponseList(exchangeRateRepository.saveAll(rates));
        }
    }

    @Transactional
    public Boolean existsExpiredOrMissing() {
        return exchangeRateRepository.existsExpiredOrMissing(Instant.now());
    }
}
