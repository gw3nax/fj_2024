package ru.gw3nax.currency_exchanger.exceptions;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CBRUnavailableException extends RuntimeException {
    private final Map<String, String> headers = new HashMap<>();

    public CBRUnavailableException(String message) {
        super(message);
    }

    public CBRUnavailableException addHeader(String message, String cause) {
        headers.put(message, cause);
        return this;
    }
}
