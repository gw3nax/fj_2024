package ru.gw3nax.kudagoapi.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequest {
    String fromCurrency;
    String toCurrency;
    Double amount;
}
