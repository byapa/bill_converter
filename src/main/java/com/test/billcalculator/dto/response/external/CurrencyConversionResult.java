package com.test.billcalculator.dto.response.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyConversionResult {
    private String result;
    private String errorMessage;

    @JsonProperty("conversion_rate")
    private BigDecimal conversionRate;
}
