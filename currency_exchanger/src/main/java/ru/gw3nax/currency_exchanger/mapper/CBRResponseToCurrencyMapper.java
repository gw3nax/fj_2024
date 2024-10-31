package ru.gw3nax.currency_exchanger.mapper;

import ru.gw3nax.currency_exchanger.client.dto.CBRResponse;
import ru.gw3nax.currency_exchanger.controller.dto.CurrencyResponse;

public class CBRResponseToCurrencyMapper {
    public static CurrencyResponse map(CBRResponse cbrResponse) {
       return CurrencyResponse.builder()
                .charCode(cbrResponse.getCharCode())
                .value(Double.parseDouble(cbrResponse.getValue().replace(',', '.')))
                .build();
    }
}
