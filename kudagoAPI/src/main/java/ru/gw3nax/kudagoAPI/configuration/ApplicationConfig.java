package ru.gw3nax.kudagoAPI.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.kudago.api", ignoreUnknownFields = false)
public record ApplicationConfig(
        String locationBaseUrl,
        String categoryBaseUrl
) {
}
