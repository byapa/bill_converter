package com.test.billcalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    private List<BillItem> items;
    private BigDecimal totalAmount;
    private String currencyCode;
    private User customer;
}