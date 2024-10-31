package ru.gw3nax.currency_exchanger.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gw3nax.currency_exchanger.client.CBRClient;
import ru.gw3nax.currency_exchanger.client.dto.CBRResponse;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyRequest;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyResponse;
import ru.gw3nax.currency_exchanger.controller.dto.CurrencyResponse;
import ru.gw3nax.currency_exchanger.exceptions.IllegalRequestArgumentsException;
import ru.gw3nax.currency_exchanger.exceptions.NotExistingCharCodeException;
import ru.gw3nax.currency_exchanger.exceptions.NotFoundException;
import ru.gw3nax.currency_exchanger.mapper.CBRResponseToCurrencyMapper;
import ru.gw3nax.currency_exchanger.mapper.ConvertCurrencyResponseMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CurrencyExchangeService {
    private static final String RUB_CODE = "RUB";
    CBRClient cbrClient;

    @Autowired
    CurrencyExchangeService(CBRClient cbrClient) {
        this.cbrClient = cbrClient;
    }

    private static void validateRequest(ConvertCurrencyRequest convertCurrencyRequest) {
        validateCharCode(convertCurrencyRequest.getToCurrency());
        validateCharCode(convertCurrencyRequest.getFromCurrency());
        if (convertCurrencyRequest.getAmount() < 0) {
            throw new IllegalRequestArgumentsException("Amount must be greater than zero");
        }
    }

    public static void validateCharCode(String charCode) {
        try {
            Currency.getInstance(charCode);
        } catch (IllegalArgumentException e) {
            throw new NotExistingCharCodeException("Currency does not exist.");
        }
    }

    private static CurrencyResponse findNeededCurrency(List<CBRResponse> currencyRates, String charCode) {
        CBRResponse cbrResponse = currencyRates.stream()
                .filter(cbrResponse1 -> Objects.equals(cbrResponse1.getCharCode(), charCode))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Currency not found"));
        return CBRResponseToCurrencyMapper.map(cbrResponse);
    }

    public CurrencyResponse getCurrencyRate(String currencyCode) {
        validateCharCode(currencyCode);

        List<CBRResponse> currencyRates = cbrClient.getCurrencies();
        return findNeededCurrency(currencyRates, currencyCode);
    }

    public ConvertCurrencyResponse convertCurrency(ConvertCurrencyRequest convertCurrencyRequest) {
        validateRequest(convertCurrencyRequest);

        List<CBRResponse> currencyRates = cbrClient.getCurrencies();
        CurrencyResponse fromCurrencyRate;
        CurrencyResponse toCurrencyRate;
        double convertedAmount;

        if (RUB_CODE.equals(convertCurrencyRequest.getFromCurrency())) {
            toCurrencyRate = findNeededCurrency(currencyRates, convertCurrencyRequest.getToCurrency());
            convertedAmount = convertCurrencyRequest.getAmount() / toCurrencyRate.getValue();
            return ConvertCurrencyResponseMapper.mapToCurrencyResponse(
                    convertCurrencyRequest,
                    new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP).doubleValue()
            );
        } else if (RUB_CODE.equals(convertCurrencyRequest.getToCurrency())) {
            fromCurrencyRate = findNeededCurrency(currencyRates, convertCurrencyRequest.getFromCurrency());
            convertedAmount = convertCurrencyRequest.getAmount() * fromCurrencyRate.getValue();
            return ConvertCurrencyResponseMapper.mapToCurrencyResponse(
                    convertCurrencyRequest,
                    new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP).doubleValue()
            );
        } else {
            fromCurrencyRate = findNeededCurrency(currencyRates, convertCurrencyRequest.getFromCurrency());
            toCurrencyRate = findNeededCurrency(currencyRates, convertCurrencyRequest.getToCurrency());
            convertedAmount = fromCurrencyRate.getValue() * convertCurrencyRequest.getAmount() / toCurrencyRate.getValue();
            return ConvertCurrencyResponseMapper.mapToCurrencyResponse(
                    convertCurrencyRequest,
                    new BigDecimal(convertedAmount).setScale(2, RoundingMode.HALF_UP).doubleValue()
            );
        }
    }
}
