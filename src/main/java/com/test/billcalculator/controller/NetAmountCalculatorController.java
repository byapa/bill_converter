package com.test.billcalculator.controller;

import com.test.billcalculator.dto.request.NetPayableAmountCalculationRequestDto;
import com.test.billcalculator.dto.response.NetPayableAmountResponseDto;
import com.test.billcalculator.service.BillCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NetAmountCalculatorController {
    private final BillCalculatorService billCalculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<NetPayableAmountResponseDto> calculateTotalPayableAmount(
            @RequestBody NetPayableAmountCalculationRequestDto request) {

        BigDecimal totalPayable = billCalculatorService.calculateTotalPayable(request.getBill(),
                request.getTargetCurrencyCode());

        // Return the result in the response
        NetPayableAmountResponseDto result = new NetPayableAmountResponseDto(
                totalPayable, request.getTargetCurrencyCode());

        return ResponseEntity.ok(result);
    }
}
