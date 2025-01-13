package com.test.billcalculator.service;

import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convertTo(BigDecimal bigDecimal, String originalCurrencyCode, String targetCurrencyCode);
}
