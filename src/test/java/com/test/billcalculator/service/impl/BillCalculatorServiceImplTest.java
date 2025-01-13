package com.test.billcalculator.service.impl;

import com.test.billcalculator.exception.InvalidInputException;
import com.test.billcalculator.model.Bill;
import com.test.billcalculator.model.BillItem;
import com.test.billcalculator.service.BillCalculatorService;
import com.test.billcalculator.service.CurrencyConversionService;
import com.test.billcalculator.service.DiscountCalculatorService;
import com.test.billcalculator.util.constant.ItemCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BillCalculatorServiceImplTest {

    @Mock
    private DiscountCalculatorService discountCalculatorService;

    @Mock
    private CurrencyConversionService currencyConversionService;

    private BillCalculatorService billCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        billCalculatorService = new BillCalculatorServiceImpl(discountCalculatorService, currencyConversionService);
    }

    @Test
    void testCalculatePayableAmount_withValidInputs() {
        // Given
        String originalCurrencyCode = "USD";
        String targetCurrencyCode = "AED";
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("800.0"));
        bill.setCurrencyCode(originalCurrencyCode);

        when(discountCalculatorService.calculateDiscount(bill)).thenReturn(new BigDecimal("200.00"));
        when(currencyConversionService.convertTo(new BigDecimal("600.00"), originalCurrencyCode, targetCurrencyCode)).thenReturn(new BigDecimal("2200.00"));

        // When
        BigDecimal totalPayable = billCalculatorService.calculateTotalPayable(bill, targetCurrencyCode);

        // Then
        assertEquals(new BigDecimal("2200.00"), totalPayable);
    }

    @Test
    void testCalculatePayableAmount_throwsInvalidInputExceptionForNullBill() {
        // Given
        String targetCurrencyCode = "AED";

        // When
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> billCalculatorService.calculateTotalPayable(null, targetCurrencyCode));

        // Then
        assertEquals("Bill cannot be null", exception.getMessage());
    }

    @Test
    void testCalculatePayableAmount_throwsInvalidInputExceptionForNullTotalAmount() {
        // Given
        Bill bill = new Bill();
        bill.setTotalAmount(null);
        bill.setCurrencyCode("USD");

        String targetCurrencyCode = "AED";

        // When
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> billCalculatorService.calculateTotalPayable(bill, targetCurrencyCode));

        // Then
        assertEquals("Total amount cannot be null", exception.getMessage());
    }

    @Test
    void testCalculatePayableAmount_throwsInvalidInputExceptionForNullCurrencyCode() {
        // Given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("800.0"));
        bill.setCurrencyCode(null);

        String targetCurrencyCode = "AED";

        // When
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> billCalculatorService.calculateTotalPayable(bill, targetCurrencyCode));

        // Then
        assertEquals("Currency code cannot be null", exception.getMessage());
    }

    @Test
    void testCalculatePayableAmount_throwsInvalidInputExceptionForNullTargetCurrencyCode() {
        // Given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("800.0"));
        bill.setCurrencyCode("USD");

        // When
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> billCalculatorService.calculateTotalPayable(bill, null));

        // Then
        assertEquals("Target currency code cannot be null", exception.getMessage());
    }

    @Test
    void testCalculatePayableAmount_throwsInvalidInputExceptionWhenItemAmountSumDoesNotMatchBillTotalAmount() {
        // Given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("200.0"));
        bill.setCurrencyCode("USD");
        bill.setItems(Arrays.asList(
                new BillItem("item1", ItemCategory.ELECTRONICS, new BigDecimal("150.00")),
                new BillItem("item1", ItemCategory.POWER_TOOLS, new BigDecimal("100.00"))
        ));

        // When
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> billCalculatorService.calculateTotalPayable(bill, "AED"));

        // Then
        assertEquals("Sum of individual item amount does not match the total bill amount", exception.getMessage());
    }
}
