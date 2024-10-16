package ru.gw3nax.currency_exchanger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gw3nax.currency_exchanger.controller.dto.ErrorResponse;
import ru.gw3nax.currency_exchanger.exceptions.CBRUnavailableException;
import ru.gw3nax.currency_exchanger.exceptions.IllegalRequestArgumentsException;
import ru.gw3nax.currency_exchanger.exceptions.NotExistingCharCodeException;
import ru.gw3nax.currency_exchanger.exceptions.NotFoundException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorResponseWrapper {

    @ExceptionHandler({CBRUnavailableException.class})
    public ResponseEntity<ErrorResponse> handleCBRUnavailableException(CBRUnavailableException e) {
        log.error("CBRUnavailableException was caught with reason: {}", e.getMessage());
        var errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode(500);

        var header = new HttpHeaders();
        for (Map.Entry<String, String> entry : e.getHeaders().entrySet()) {
            header.add(entry.getKey(), entry.getValue());
        }
       return new ResponseEntity<>(errorResponse, header, HttpStatus.valueOf(500));
    }

    @ExceptionHandler({IllegalRequestArgumentsException.class})
    public ResponseEntity<ErrorResponse> handleIllegalRequestArgumentsException(IllegalRequestArgumentsException e) {
        log.error("IllegalRequestArgumentsException was caught with reason: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException was caught with reason: {}", e.getMessage());
        return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));
    }

    @ExceptionHandler({NotExistingCharCodeException.class})
    public ResponseEntity<ErrorResponse> handleNotExistingCharCodeException(NotExistingCharCodeException e) {
        log.error("NotExistingCharCodeException was caught with reason: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
    }
}
