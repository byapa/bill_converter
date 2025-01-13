package com.test.billcalculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NetAmountCalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testNetAmountCalculator_success() throws Exception {
        String payload = """
                    {
                        "bill": {
                            "totalAmount": 100.00,
                            "currencyCode": "USD"
                        },
                        "targetCurrencyCode": "AED"
                    }
                """;
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("AED"));
    }

    @Test
    void testNetAmountCalculator_errorWhenBillIsNull() throws Exception {
        String payload = """
                    {
                        "bill": null,
                        "targetCurrencyCode": "AED"
                    }
                """;
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNetAmountCalculator_errorWhenTargetCurrencyCodeIsNull() throws Exception {
        String payload = """
                    {
                        "bill": {
                            "totalAmount": 100.00,
                            "currencyCode": "USD"
                        },
                        "targetCurrencyCode": null
                    }
                """;
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNetAmountCalculator_errorWhenTargetCurrencyCodeIsInvalid() throws Exception {
        String payload = """
                    {
                        "bill": {
                            "totalAmount": 100.00,
                            "currencyCode": "USD"
                        },
                        "targetCurrencyCode": "XYZ"
                    }
                """;
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

}
