package ru.gw3nax.currency_exchanger.mapper;

import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyRequest;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyResponse;

public class ConvertCurrencyResponseMapper {
    public static ConvertCurrencyResponse mapToCurrencyResponse(ConvertCurrencyRequest convertCurrencyRequest, Double convertedAmount){
        return ConvertCurrencyResponse.builder()
                .convertedAmount(convertedAmount)
                .fromCurrency(convertCurrencyRequest.getFromCurrency())
                .toCurrency(convertCurrencyRequest.getToCurrency())
                .build();
    }
}
