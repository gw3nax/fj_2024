package ru.gw3nax.kudagoapi.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String e) {
        super(e);
    }
}
