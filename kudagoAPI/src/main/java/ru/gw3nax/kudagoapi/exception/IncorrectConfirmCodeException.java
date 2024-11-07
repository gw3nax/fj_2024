package ru.gw3nax.kudagoapi.exception;

public class IncorrectConfirmCodeException extends IncorrectPasswordException {
    public IncorrectConfirmCodeException(String e) {
        super(e);
    }
}
