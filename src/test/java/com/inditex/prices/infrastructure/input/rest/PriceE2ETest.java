package com.inditex.prices.infrastructure.input.rest;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Full-stack E2E tests using RestAssured's spring-mock-mvc module.
 *
 * Uses RestAssured's fluent given/when/then DSL backed by Spring MockMvc
 * (no real HTTP socket, no proxy detection issues). The full application
 * context is loaded, so this exercises controller, service, and repository
 * layers end-to-end.
 *
 * @ActiveProfiles("test") loads application-test.yml for quieter logging.
 * data.sql is executed automatically via spring.sql.init.mode=always.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    private static final String BASE_URL = "/api/v1/prices";
    private static final int PRODUCT_ID = 35455;
    private static final int BRAND_ID = 1;

    @Test
    void givenJune14At10h_whenGetPrice_thenReturnsPriceList1At35_50() {
        given()
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(200)
                .body("productId", equalTo(PRODUCT_ID))
                .body("brandId", equalTo(BRAND_ID))
                .body("priceList", equalTo(1))
                .body("price", equalTo(35.50f));
    }

    @Test
    void givenJune14At16h_whenGetPrice_thenReturnsPriceList2At25_45() {
        given()
                .param("applicationDate", "2020-06-14T16:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(200)
                .body("productId", equalTo(PRODUCT_ID))
                .body("brandId", equalTo(BRAND_ID))
                .body("priceList", equalTo(2))
                .body("price", equalTo(25.45f));
    }

    @Test
    void givenJune14At21h_whenGetPrice_thenReturnsPriceList1At35_50() {
        given()
                .param("applicationDate", "2020-06-14T21:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(200)
                .body("productId", equalTo(PRODUCT_ID))
                .body("brandId", equalTo(BRAND_ID))
                .body("priceList", equalTo(1))
                .body("price", equalTo(35.50f));
    }

    @Test
    void givenJune15At10h_whenGetPrice_thenReturnsPriceList3At30_50() {
        given()
                .param("applicationDate", "2020-06-15T10:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(200)
                .body("productId", equalTo(PRODUCT_ID))
                .body("brandId", equalTo(BRAND_ID))
                .body("priceList", equalTo(3))
                .body("price", equalTo(30.50f));
    }

    @Test
    void givenJune16At21h_whenGetPrice_thenReturnsPriceList4At38_95() {
        given()
                .param("applicationDate", "2020-06-16T21:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(200)
                .body("productId", equalTo(PRODUCT_ID))
                .body("brandId", equalTo(BRAND_ID))
                .body("priceList", equalTo(4))
                .body("price", equalTo(38.95f));
    }

    @Test
    void shouldReturn404_whenNoPriceExistsForGivenParameters() {
        given()
                .param("applicationDate", "2019-01-01T00:00:00")
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    void shouldReturn400_whenRequiredParameterIsMissing() {
        given()
                .param("productId", PRODUCT_ID)
                .param("brandId", BRAND_ID)
        .when()
                .get(BASE_URL)
        .then()
                .statusCode(400);
    }
}
