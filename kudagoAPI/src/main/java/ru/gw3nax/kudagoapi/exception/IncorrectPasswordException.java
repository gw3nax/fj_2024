package ru.gw3nax.kudagoapi.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String incorrectPassword) {
        super(incorrectPassword);
    }
}
