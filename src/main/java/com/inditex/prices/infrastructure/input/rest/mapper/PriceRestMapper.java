package com.inditex.prices.infrastructure.input.rest.mapper;

import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.input.rest.dto.PriceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceRestMapper {

    PriceResponse toDto(Price price);
}
