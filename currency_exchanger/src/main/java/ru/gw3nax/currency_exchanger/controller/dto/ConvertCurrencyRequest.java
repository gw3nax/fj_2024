package ru.gw3nax.currency_exchanger.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertCurrencyRequest {
    private String fromCurrency;
    private String toCurrency;
    private Double amount;
}
