package com.inditex.prices.infrastructure.output.persistence;

import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.output.persistence.mapper.PricePersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the persistence adapter.
 *
 * @DataJpaTest spins up only JPA/Hibernate beans + H2.
 * @Import brings in the MapStruct-generated mapper and the adapter under test.
 * @Sql loads sample data before each test within the auto-rolled-back transaction.
 */
@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Sql("/data.sql")
@Import({PricePersistenceMapperImpl.class, PricePersistenceAdapter.class})
class PricePersistenceAdapterTest {

    @Autowired
    private PricePersistenceAdapter adapter;

    @Test
    void shouldReturnPriceList1_atTenAMOnJune14() {
        Optional<Price> result = adapter.findApplicablePrice(
                1L, 35455L, LocalDateTime.of(2020, 6, 14, 10, 0));

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(1);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    void shouldReturnPriceList2_atFourPMOnJune14() {
        Optional<Price> result = adapter.findApplicablePrice(
                1L, 35455L, LocalDateTime.of(2020, 6, 14, 16, 0));

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(2);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("25.45"));
    }

    @Test
    void shouldReturnPriceList1_atNinePMOnJune14() {
        Optional<Price> result = adapter.findApplicablePrice(
                1L, 35455L, LocalDateTime.of(2020, 6, 14, 21, 0));

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(1);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("35.50"));
    }

    @Test
    void shouldReturnPriceList3_atTenAMOnJune15() {
        Optional<Price> result = adapter.findApplicablePrice(
                1L, 35455L, LocalDateTime.of(2020, 6, 15, 10, 0));

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(3);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("30.50"));
    }

    @Test
    void shouldReturnPriceList4_atNinePMOnJune16() {
        Optional<Price> result = adapter.findApplicablePrice(
                1L, 35455L, LocalDateTime.of(2020, 6, 16, 21, 0));

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(4);
        assertThat(result.get().price()).isEqualByComparingTo(new BigDecimal("38.95"));
    }

    @Test
    void shouldReturnEmpty_whenNoPriceMatchesApplicationDate() {
        Optional<Price> result = adapter.findApplicablePrice(
                1L, 35455L, LocalDateTime.of(2019, 1, 1, 0, 0));

        assertThat(result).isEmpty();
    }
}
