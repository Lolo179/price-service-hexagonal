package com.inditex.prices.infrastructure.output.persistence;

import com.inditex.prices.application.port.out.PriceRepositoryPort;
import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.output.persistence.mapper.PricePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PricePersistenceAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository priceJpaRepository;
    private final PricePersistenceMapper pricePersistenceMapper;

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceJpaRepository.findTop1ByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        brandId, productId, applicationDate, applicationDate)
                .map(pricePersistenceMapper::toDomain);
    }
}
