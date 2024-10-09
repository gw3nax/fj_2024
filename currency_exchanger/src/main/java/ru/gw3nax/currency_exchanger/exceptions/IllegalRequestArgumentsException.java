package ru.gw3nax.currency_exchanger.exceptions;

public class IllegalRequestArgumentsException extends RuntimeException {
    public IllegalRequestArgumentsException(String message) {
        super(message);
    }
}
