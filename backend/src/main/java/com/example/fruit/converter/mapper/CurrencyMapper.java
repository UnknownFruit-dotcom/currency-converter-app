package com.example.fruit.converter.mapper;

import com.example.fruit.converter.dto.request.CurrencyCreationRequest;
import com.example.fruit.converter.dto.request.CurrencyUpdateRequest;
import com.example.fruit.converter.dto.response.CurrencyResponse;
import com.example.fruit.converter.model.Currency;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    @Mapping(target = "id", ignore = true)
    Currency toEntity(CurrencyCreationRequest request);

    CurrencyResponse toResponse(Currency currency);

    List<CurrencyResponse> toResponseList(List<Currency> currencies);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCurrencyFromDto(CurrencyUpdateRequest dto, @MappingTarget Currency entity);
}
