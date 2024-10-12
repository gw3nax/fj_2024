package ru.gw3nax.currency_exchanger.exceptions;

public class NotExistingCharCodeException extends RuntimeException {
    public NotExistingCharCodeException(final String message) {
        super(message);
    }
}
