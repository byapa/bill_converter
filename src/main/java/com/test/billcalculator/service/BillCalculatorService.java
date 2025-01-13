package com.test.billcalculator.service;

import com.test.billcalculator.model.Bill;

import java.math.BigDecimal;

public interface BillCalculatorService {
    BigDecimal calculateTotalPayable(Bill bill, String targetCurrencyCode);
}
