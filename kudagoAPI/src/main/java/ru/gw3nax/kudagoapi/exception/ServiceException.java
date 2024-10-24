package ru.gw3nax.kudagoapi.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {
    private final Integer code;

    public ServiceException(String text, Integer code) {
        super(text);
        this.code = code;
    }
}
