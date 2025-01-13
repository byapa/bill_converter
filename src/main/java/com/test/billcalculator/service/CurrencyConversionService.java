package com.test.billcalculator.service;

import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convertTo(BigDecimal originalAmount, String originalCurrencyCode, String targetCurrencyCode);
}
