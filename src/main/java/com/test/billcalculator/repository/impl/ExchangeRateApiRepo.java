package com.test.billcalculator.repository.impl;

import com.test.billcalculator.dto.response.external.CurrencyConversionResult;
import com.test.billcalculator.exception.CurrencyConversionException;
import com.test.billcalculator.repository.ExchangeRateRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateApiRepo implements ExchangeRateRepo {

    private final RestTemplate restTemplate;

    @Value("${exchangerate.api.key}")
    private String apiKey;

    @Value("${exchangerate.api.url}")
    private String apiUrl;

    @Override
    @Cacheable("exchangeRateCache")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {

        String url = UriComponentsBuilder.fromUriString(apiUrl)
                .buildAndExpand(apiKey, fromCurrency, toCurrency)
                .toUriString();

        // Ref: https://www.exchangerate-api.com/docs/pair-conversion-requests
        log.info("Calling exchange rate API: {}", url);

        CurrencyConversionResult result =  restTemplate.getForObject(url, CurrencyConversionResult.class);

        if (result == null) {
            throw new CurrencyConversionException("Currency conversion failed due to unknown reason");
        }

        if (!result.getResult().equals("success")) {
            throw new CurrencyConversionException("Currency conversion failed: " + result.getErrorMessage());
        }

        return result.getConversionRate();
    }
}
