package ru.gw3nax.currency_exchanger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.cbr", ignoreUnknownFields = false)
public record ApplicationConfig(
        String cbrBaseUrl
) {
}
