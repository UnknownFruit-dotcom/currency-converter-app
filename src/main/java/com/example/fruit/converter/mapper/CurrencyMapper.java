package com.example.fruit.converter.mapper;

import com.example.fruit.converter.dto.response.CurrencyResponse;
import com.example.fruit.converter.model.Currency;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyResponse toResponse(Currency currency);

    List<CurrencyResponse> toResponseList(List<Currency> currencies);
}
