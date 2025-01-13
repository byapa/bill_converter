package com.test.billcalculator.dto.request;

import com.test.billcalculator.model.Bill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NetPayableAmountCalculationRequestDto {
    @NotBlank(message = "Target currency is required")
    private String targetCurrencyCode;

    @NotNull(message = "Bill data are required")
    private Bill bill;
}
