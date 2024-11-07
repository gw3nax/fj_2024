package ru.gw3nax.kudagoapi.exception;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String e) {
        super(e);
    }
}
