package com.test.billcalculator.service.impl;

import com.test.billcalculator.repository.ExchangeRateRepo;
import com.test.billcalculator.service.CurrencyConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyConversionService {

    private final ExchangeRateRepo exchangeRateRepo;

    @Override
    public BigDecimal convertTo(BigDecimal originalAmount, String originalCurrencyCode, String targetCurrencyCode) {

        BigDecimal conversionRate = exchangeRateRepo.getExchangeRate(originalCurrencyCode, targetCurrencyCode);

        return originalAmount.multiply(conversionRate);
    }
}
