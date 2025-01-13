package com.test.billcalculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NetPayableAmountResponseDto {
    private BigDecimal netPayableAmount;
    private String currencyCode;
}
