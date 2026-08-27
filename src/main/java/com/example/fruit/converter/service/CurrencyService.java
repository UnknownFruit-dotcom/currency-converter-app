package com.example.fruit.converter.service;

import com.example.fruit.converter.dto.response.CurrencyResponse;
import com.example.fruit.converter.mapper.CurrencyMapper;
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

    public List<CurrencyResponse> getAllCurrencies() { return currencyMapper.toResponseList(currencyRepository.findAll()); }

    public CurrencyResponse getCurrencyById(Long id) { return currencyMapper.toResponse(currencyRepository.getReferenceById(id)); }
}
