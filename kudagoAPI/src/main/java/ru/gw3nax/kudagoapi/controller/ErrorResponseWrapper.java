package ru.gw3nax.kudagoapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gw3nax.kudagoapi.controller.dto.ErrorResponse;
import ru.gw3nax.kudagoapi.exception.ServiceException;

@ControllerAdvice
public class ErrorResponseWrapper {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        return ResponseEntity
                .status(e.getCode())
                .body(new ErrorResponse(e.getMessage(), e.getCode()));
    }
}
