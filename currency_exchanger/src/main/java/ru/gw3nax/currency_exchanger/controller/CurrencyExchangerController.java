package ru.gw3nax.currency_exchanger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyRequest;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyResponse;
import ru.gw3nax.currency_exchanger.controller.dto.CurrencyResponse;
import ru.gw3nax.currency_exchanger.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currencies")
@Slf4j
public class CurrencyExchangerController {

    CurrencyExchangeService currencyExchangeService;

    @Autowired
    CurrencyExchangerController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/rates/{currencyCode}")
    public CurrencyResponse getCurrencyRate(@PathVariable String currencyCode) {
        return currencyExchangeService.getCurrencyRate(currencyCode);
    }

    @PostMapping("/convert")
    public ConvertCurrencyResponse postForConvertCurrency(@RequestBody ConvertCurrencyRequest convertCurrencyRequest) {
        log.info("Currency conversion started");
        return currencyExchangeService.convertCurrency(convertCurrencyRequest);
    }
}
