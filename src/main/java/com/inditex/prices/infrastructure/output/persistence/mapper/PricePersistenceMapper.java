package com.inditex.prices.infrastructure.output.persistence.mapper;

import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.output.persistence.PriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PricePersistenceMapper {

    Price toDomain(PriceEntity entity);
}
