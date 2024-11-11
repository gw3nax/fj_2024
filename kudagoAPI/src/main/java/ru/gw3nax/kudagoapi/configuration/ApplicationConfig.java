package ru.gw3nax.kudagoapi.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        String locationBaseUrl,
        String categoryBaseUrl,
        Integer fixedThreadPoolSize,
        Duration duration,
        String eventsBaseUrl,
        String currencyBaseUrl,
        Integer maxRequests
) {
}
