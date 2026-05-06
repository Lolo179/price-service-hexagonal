package com.inditex.prices.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI priceServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Price Service API")
                        .description("REST service to query applicable prices for Inditex products")
                        .version("1.0.0"));
    }
}
