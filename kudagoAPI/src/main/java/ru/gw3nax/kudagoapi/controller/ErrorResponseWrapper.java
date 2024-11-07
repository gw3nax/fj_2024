package ru.gw3nax.kudagoapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gw3nax.kudagoapi.controller.dto.ErrorResponse;
import ru.gw3nax.kudagoapi.exception.IncorrectConfirmCodeException;
import ru.gw3nax.kudagoapi.exception.IncorrectPasswordException;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.exception.UserAlreadyExistException;

@ControllerAdvice
public class ErrorResponseWrapper {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        return ResponseEntity
                .status(e.getCode())
                .body(new ErrorResponse(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException e) {

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .text(e.getMessage())
                        .code(400)
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {

        var response = ErrorResponse.builder()
                .text(e.getMessage())
                .code(404)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException e) {
        var response = ErrorResponse.builder()
                .text(e.getMessage())
                .code(401)
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IncorrectConfirmCodeException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectConfirmCodeException(IncorrectConfirmCodeException e) {
        var response = ErrorResponse.builder()
                .text(e.getMessage())
                .code(401)
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
