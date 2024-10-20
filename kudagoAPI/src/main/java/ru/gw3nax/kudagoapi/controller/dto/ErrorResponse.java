package ru.gw3nax.kudagoapi.controller.dto;

public record ErrorResponse(
        String text,
        Integer code
) {
}
