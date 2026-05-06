package com.inditex.prices.application.service;

import com.inditex.prices.application.port.in.GetApplicablePriceUseCase;
import com.inditex.prices.application.port.out.PriceRepositoryPort;
import com.inditex.prices.domain.exception.PriceNotFoundException;
import com.inditex.prices.domain.model.Price;
import com.inditex.prices.shared.exception.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        log.debug("Looking up price: brandId={}, productId={}, applicationDate={}", brandId, productId, applicationDate);
        return priceRepositoryPort.findApplicablePrice(brandId, productId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(
                        String.format(ErrorMessages.PRICE_NOT_FOUND, brandId, productId,
                                applicationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}
