package com.inditex.prices.infrastructure.input.rest;

import com.inditex.prices.application.port.in.GetApplicablePriceUseCase;
import com.inditex.prices.infrastructure.input.rest.api.PricesApi;
import com.inditex.prices.infrastructure.input.rest.dto.PriceResponse;
import com.inditex.prices.infrastructure.input.rest.mapper.PriceRestMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequiredArgsConstructor
public class PriceController implements PricesApi {

    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;
    private final PriceRestMapper priceRestMapper;

    @Override
    public ResponseEntity<PriceResponse> getApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId) {
        log.info("GET /api/v1/prices - productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);
        PriceResponse response = priceRestMapper.toDto(
                getApplicablePriceUseCase.getApplicablePrice(brandId, productId, applicationDate)
        );
        log.debug("Response: priceList={}, price={}, currency={}", response.getPriceList(), response.getPrice(), response.getCurrency());
        return ResponseEntity.ok(response);
    }
}
