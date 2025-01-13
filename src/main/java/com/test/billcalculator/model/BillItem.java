package com.test.billcalculator.model;

import com.test.billcalculator.util.constant.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
class BillItem {
    private String name;
    private ItemCategory category;
}
