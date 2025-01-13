package com.test.billcalculator.repository;

import java.math.BigDecimal;

public interface ExchangeRateRepo {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);
}
