package ru.gw3nax.currency_exchanger.mapper;

import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyRequest;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyResponse;

public class ConvertCurrencyResponseMapper {
    public static ConvertCurrencyResponse mapToCurrencyResponse(ConvertCurrencyRequest convertCurrencyRequest, Double convertedAmount){
        ConvertCurrencyResponse convertCurrencyResponse = new ConvertCurrencyResponse();
        convertCurrencyResponse.setConvertedAmount(convertedAmount);
        convertCurrencyResponse.setFromCurrency(convertCurrencyRequest.getFromCurrency());
        convertCurrencyResponse.setToCurrency(convertCurrencyRequest.getToCurrency());
        return convertCurrencyResponse;
    }
}
