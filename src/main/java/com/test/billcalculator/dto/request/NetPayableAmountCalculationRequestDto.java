package com.test.billcalculator.dto.request;

import com.test.billcalculator.model.Bill;
import lombok.Data;

@Data
public class NetPayableAmountCalculationRequestDto {
    private String targetCurrencyCode;
    private Bill bill;
}
