package ru.gw3nax.kudagoapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        String locationBaseUrl,
        String categoryBaseUrl,
        Integer fixedThreadPoolSize,
        Long duration,
        String eventsBaseUrl,
        String currencyBaseUrl,
        Integer maxRequests
) {
}
