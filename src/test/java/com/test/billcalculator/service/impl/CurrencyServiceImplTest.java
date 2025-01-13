package com.test.billcalculator.service.impl;

import com.test.billcalculator.repository.ExchangeRateRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CurrencyServiceImplTest {

    @Mock
    private ExchangeRateRepo exchangeRateRepo;

    @InjectMocks
    private CurrencyConversionServiceImpl currencyConversionService;

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

        BigDecimal conversionRate = new BigDecimal("3.543");
        when(exchangeRateRepo.getExchangeRate(fromCurrency, toCurrency))
                .thenReturn(conversionRate);

        // When
        BigDecimal result = currencyConversionService.convertTo(amount, fromCurrency, toCurrency);

        // Then
        assertEquals(amount.multiply(conversionRate), result);
    }


}
