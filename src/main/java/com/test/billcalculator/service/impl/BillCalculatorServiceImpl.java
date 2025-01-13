package com.test.billcalculator.service.impl;

import com.test.billcalculator.exception.InvalidInputException;
import com.test.billcalculator.model.Bill;
import com.test.billcalculator.service.BillCalculatorService;
import com.test.billcalculator.service.CurrencyConversionService;
import com.test.billcalculator.service.DiscountCalculatorService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Data
public class BillCalculatorServiceImpl implements BillCalculatorService {

    private final DiscountCalculatorService discountCalculatorService;
    private final CurrencyConversionService currencyConversionService;

    @Override
    public BigDecimal calculateTotalPayable(Bill bill, String targetCurrencyCode) {
        this.validateInputsOfTotalPayableCalculation(bill, targetCurrencyCode);
        BigDecimal discount = discountCalculatorService.calculateDiscount(bill);
        BigDecimal totalPayableAmount = bill.getTotalAmount().subtract(discount);
        return currencyConversionService.convertTo(totalPayableAmount, bill.getCurrencyCode(), targetCurrencyCode);
    }

    private void validateInputsOfTotalPayableCalculation(Bill bill, String targetCurrencyCode)
            throws InvalidInputException {
        if (targetCurrencyCode == null) {
            throw new InvalidInputException("Target currency code cannot be null");
        }

        if (bill == null) {
            throw new InvalidInputException("Bill cannot be null");
        }

        if (bill.getTotalAmount() == null) {
            throw new InvalidInputException("Total amount cannot be null");
        }

        if (bill.getCurrencyCode() == null) {
            throw new InvalidInputException("Currency code cannot be null");
        }
    }
}
