package com.test.billcalculator.service.impl;

import com.test.billcalculator.dto.response.external.ConversionResponseOfExchangeApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExchangeRateApiBasedConversionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateApiBasedConversionService currencyConversionService;

    @Value("${exchangerate.api.url}")
    private String apiUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCurrencyConversionService() {
        // Given
        String fromCurrency = "USD";
        String toCurrency = "AED";
        BigDecimal amount = new BigDecimal("1000.00");

        ConversionResponseOfExchangeApi mockResponse = new ConversionResponseOfExchangeApi();
        mockResponse.setResult("success");
        mockResponse.setConversionResult(new BigDecimal("8500.00"));

        when(restTemplate.getForObject(apiUrl, ConversionResponseOfExchangeApi.class))
                .thenReturn(mockResponse);

        // When
        BigDecimal result = currencyConversionService.convertTo(amount, fromCurrency, toCurrency);

        // Then
        assertEquals(new BigDecimal("8500.00"), result);
    }


}
