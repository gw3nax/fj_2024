package ru.gw3nax.currency_exchanger.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertCurrencyRequest {
    String fromCurrency;
    String toCurrency;
    Double amount;
}
