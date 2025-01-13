package com.test.billcalculator.service.impl;

import com.test.billcalculator.model.Bill;
import com.test.billcalculator.model.BillItem;
import com.test.billcalculator.model.User;
import com.test.billcalculator.service.DiscountCalculatorService;
import com.test.billcalculator.util.constant.ItemCategory;
import com.test.billcalculator.util.constant.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountCalculatorServiceImplTest {

    private DiscountCalculatorService discountCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        discountCalculatorService = new DiscountCalculatorServiceImpl();
    }

    @Test
    void testCalculatePayableAmount_whenCustomerIsAnEmployee() {
        Bill bill = new Bill();
        User customer = new User();
        customer.setUserType(UserType.EMPLOYEE);
        customer.setUserTenure(3.0f);
        bill.setCustomer(customer);
        bill.setTotalAmount(new BigDecimal("85.00"));
        bill.setCurrencyCode("USD");
        bill.setItems(Arrays.asList(
                new BillItem("item1", ItemCategory.ELECTRONICS, new BigDecimal("50.00")),
                new BillItem("item2", ItemCategory.COSMETIC, new BigDecimal("35.00"))
        ));

        BigDecimal discountedAmount = discountCalculatorService.calculateDiscount(bill);

        // Employee discount should be applied: 30% of 85 = 25.5
        BigDecimal expectedAmount = BigDecimal.valueOf(25.5);
        assertEquals(expectedAmount.setScale(2), discountedAmount);
    }

    @Test
    void testCalculatePayableAmount_whenCustomerIsAnAffiliate() {
        // Given
        Bill bill = new Bill();
        User customer = new User();
        customer.setUserType(UserType.AFFILIATE);
        customer.setUserTenure(3.0f);
        bill.setCustomer(customer);
        bill.setTotalAmount(new BigDecimal("85.00"));
        bill.setCurrencyCode("USD");
        bill.setItems(Arrays.asList(
                new BillItem("item1", ItemCategory.ELECTRONICS, new BigDecimal("50.00")),
                new BillItem("item2", ItemCategory.COSMETIC, new BigDecimal("35.00"))
        ));

        // When
        BigDecimal discountedAmount = discountCalculatorService.calculateDiscount(bill);

        // Then
        // Affiliate discount should be applied: 10% of 85 = 8.5
        BigDecimal expectedAmount = BigDecimal.valueOf(8.5);
        assertEquals(expectedAmount.setScale(2), discountedAmount);
    }

    @Test
    void testCalculatePayableAmount_whenCustomerIsNotEmployeeOrAffiliateButTenureExceedsTwoYears() {
        // Given
        Bill bill = new Bill();
        User customer = new User();
        customer.setUserType(null);
        customer.setUserTenure(3.0f);
        bill.setCustomer(customer);
        bill.setTotalAmount(new BigDecimal("85.00"));
        bill.setCurrencyCode("USD");
        bill.setItems(Arrays.asList(
                new BillItem("item1", ItemCategory.ELECTRONICS, new BigDecimal("50.00")),
                new BillItem("item2", ItemCategory.COSMETIC, new BigDecimal("35.00"))
        ));

        // When
        BigDecimal discountedAmount = discountCalculatorService.calculateDiscount(bill);

        // Then
        // Loyalty discount should be applied: 5% of 85 = 4.25
        BigDecimal expectedAmount = BigDecimal.valueOf(4.25);
        assertEquals(expectedAmount.setScale(2), discountedAmount);
    }

    @Test
    void testCalculatePayableAmount_groceriesAreExcludedFromPercentageDiscounts() {
        // Given
        Bill bill = new Bill();
        User customer = new User();
        customer.setUserType(null);
        customer.setUserTenure(3.0f);
        bill.setCustomer(customer);
        bill.setTotalAmount(new BigDecimal("95.00"));
        bill.setCurrencyCode("USD");
        bill.setItems(Arrays.asList(
                new BillItem("item1", ItemCategory.ELECTRONICS, new BigDecimal("50.00")),
                new BillItem("item2", ItemCategory.GROCERY, new BigDecimal("35.00")),
                new BillItem("item3", ItemCategory.GROCERY, new BigDecimal("10.00"))
        ));

        // When
        BigDecimal discountedAmount = discountCalculatorService.calculateDiscount(bill);

        // Then
        // Loyalty discount will be applied excluding groceries: 5% of 50 = 2.5
        BigDecimal expectedAmount = BigDecimal.valueOf(2.5);
        assertEquals(expectedAmount.setScale(2), discountedAmount);
    }

    @Test
    void testCalculatePayableAmount_whenTotalAmountExceeds100() {
        // Given
        Bill bill = new Bill();
        User customer = new User();
        customer.setUserType(null);
        customer.setUserTenure(1.5f);
        bill.setCustomer(customer);
        bill.setTotalAmount(new BigDecimal("250.00"));
        bill.setCurrencyCode("USD");
        bill.setItems(Arrays.asList(
                new BillItem("item1", ItemCategory.ELECTRONICS, new BigDecimal("150.00")),
                new BillItem("item1", ItemCategory.POWER_TOOLS, new BigDecimal("100.00"))
        ));

        // When
        BigDecimal discountedAmount = discountCalculatorService.calculateDiscount(bill);

        // Then
        // Flat discount will be applied: 5 + 5 = 10
        BigDecimal expectedAmount = BigDecimal.valueOf(10.0);
        assertEquals(expectedAmount.setScale(2), discountedAmount);
    }

}
