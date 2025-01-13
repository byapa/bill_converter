package com.test.billcalculator.service;

import com.test.billcalculator.model.Bill;

import java.math.BigDecimal;

public interface DiscountCalculatorService {
    BigDecimal calculateDiscount(Bill bill);
}
