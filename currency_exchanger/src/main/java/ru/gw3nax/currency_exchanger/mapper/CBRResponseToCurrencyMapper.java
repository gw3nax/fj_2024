package ru.gw3nax.currency_exchanger.mapper;

import ru.gw3nax.currency_exchanger.client.dto.CBRResponse;
import ru.gw3nax.currency_exchanger.controller.dto.CurrencyResponse;

public class CBRResponseToCurrencyMapper {
    public static CurrencyResponse map(CBRResponse cbrResponse) {
        CurrencyResponse currencyResponse = new CurrencyResponse();
        currencyResponse.setCharCode(cbrResponse.getCharCode());
        currencyResponse.setValue(Double.parseDouble(cbrResponse.getValue().replace(',', '.')));
        return currencyResponse;
    }
}
