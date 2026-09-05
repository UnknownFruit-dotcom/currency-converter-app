package com.example.fruit.converter.mapper;

import com.example.fruit.converter.dto.response.ExchangeRateResponse;
import com.example.fruit.converter.model.ExchangeRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {
    List<ExchangeRateResponse> toResponseList(List<ExchangeRate> rates);
}
