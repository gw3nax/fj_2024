package ru.gw3nax.currency_exchanger.exceptions;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CBRUnavailableException extends RuntimeException {
    public CBRUnavailableException(String message) {
        super(message);
    }
}
