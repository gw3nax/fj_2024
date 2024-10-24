package ru.gw3nax.kudagoapi.controller.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String text,
        Integer code
) {
}
