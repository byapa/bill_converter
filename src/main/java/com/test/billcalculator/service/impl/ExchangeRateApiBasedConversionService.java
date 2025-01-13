package com.test.billcalculator.service.impl;

import com.test.billcalculator.dto.response.external.ConversionResponseOfExchangeApi;
import com.test.billcalculator.exception.CurrencyConversionException;
import com.test.billcalculator.service.CurrencyConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ExchangeRateApiBasedConversionService implements CurrencyConversionService {

    private final RestTemplate restTemplate;

    @Value("${exchangerate.api.key}")
    private String apiKey;

    @Value("${exchangerate.api.url}")
    private String apiUrl;

    @Override
    public BigDecimal convertTo(BigDecimal originalAmount, String originalCurrencyCode, String targetCurrencyCode) {
        String url = UriComponentsBuilder.fromUriString(apiUrl)
                .buildAndExpand(apiKey, originalCurrencyCode, targetCurrencyCode, originalAmount)
                .toUriString();

        // Ref: https://www.exchangerate-api.com/docs/pair-conversion-requests
        ConversionResponseOfExchangeApi response = restTemplate.getForObject(url, ConversionResponseOfExchangeApi.class);

        if (response == null) {
            throw new CurrencyConversionException("Currency conversion failed due to unknown reason");
        }

        if (!response.getResult().equals("success")) {
            throw new CurrencyConversionException("Currency conversion failed: " + response.getErrorMessage());
        }

        return response.getConversionResult();
    }
}
