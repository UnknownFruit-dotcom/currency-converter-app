package com.example.fruit.converter.service;

import com.example.fruit.converter.dto.request.CurrencyCreationRequest;
import com.example.fruit.converter.dto.response.CurrencyResponse;
import com.example.fruit.converter.exception.ResourceNotFoundException;
import com.example.fruit.converter.mapper.CurrencyMapper;
import com.example.fruit.converter.model.Currency;
import com.example.fruit.converter.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @Transactional
    public CurrencyResponse addCurrency(CurrencyCreationRequest request) {
        var currency = currencyMapper.toEntity(request);

        Currency saved = currencyRepository.save(currency);
        return currencyMapper.toResponse(saved);
    }

    public List<CurrencyResponse> getAllCurrencies() { return currencyMapper.toResponseList(currencyRepository.findAll()); }

    public CurrencyResponse getCurrencyById(Long id) { return currencyMapper.toResponse(currencyRepository.getReferenceById(id)); }

    @Transactional
    public CurrencyResponse toggleCurrencyStatus(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));

        currency.setActive(!currency.getActive());

        return currencyMapper.toResponse(currencyRepository.save(currency));
    }

    @Transactional
    public void deleteCurrency(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));

        currencyRepository.delete(currency);
    }
}
