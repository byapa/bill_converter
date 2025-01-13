package com.test.billcalculator.service.impl;

import com.test.billcalculator.model.Bill;
import com.test.billcalculator.model.BillItem;
import com.test.billcalculator.model.User;
import com.test.billcalculator.service.DiscountCalculatorService;
import com.test.billcalculator.util.constant.ItemCategory;
import com.test.billcalculator.util.constant.UserType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class DiscountCalculatorServiceImpl implements DiscountCalculatorService {

    private static final double EMPLOYEE_DISCOUNT_PERCENTAGE = 30;
    private static final double AFFILIATE_DISCOUNT_PERCENTAGE = 10;
    private static final double LOYALTY_DISCOUNT_PERCENTAGE = 5;
    private static final float USER_TENURE_THRESHOLD = 2.0f;
    private static final double FLAT_DISCOUNT_AMOUNT = 5.00;
    private static final double BILL_DISCOUNT_THRESHOLD = 100.00;
    private static final int DISCOUNT_SCALE = 2;

    @Override
    public BigDecimal calculateDiscount(Bill bill) {
        BigDecimal flatDiscount = applyFlatDiscount(bill);
        BigDecimal percentageDiscount = applyPercentageDiscount(bill);
        return percentageDiscount.add(flatDiscount);
    }

    private BigDecimal applyFlatDiscount(Bill bill) {

        BigDecimal totalAmount = bill.getTotalAmount();
        BigDecimal flatDiscountRate = totalAmount.divideToIntegralValue(BigDecimal.valueOf(BILL_DISCOUNT_THRESHOLD));
        return BigDecimal.valueOf(FLAT_DISCOUNT_AMOUNT).multiply(flatDiscountRate).setScale(DISCOUNT_SCALE);
    }

    private BigDecimal applyPercentageDiscount(Bill bill) {

        BigDecimal totalAmount = bill.getTotalAmount();
        BigDecimal groceryTotal = calculateGroceryTotal(bill.getItems());
        BigDecimal discountableAmount = totalAmount.subtract(groceryTotal);

        User customer = bill.getCustomer();
        BigDecimal discount = BigDecimal.ZERO;
        if (UserType.EMPLOYEE == customer.getUserType()) {
            discount = discountableAmount.multiply(BigDecimal.valueOf(EMPLOYEE_DISCOUNT_PERCENTAGE / 100));
        } else if (UserType.AFFILIATE == customer.getUserType()) {
            discount = discountableAmount.multiply(BigDecimal.valueOf(AFFILIATE_DISCOUNT_PERCENTAGE / 100));
        } else if (customer.getUserTenure() > USER_TENURE_THRESHOLD) {
            discount = discountableAmount.multiply(BigDecimal.valueOf(LOYALTY_DISCOUNT_PERCENTAGE / 100));
        }

        return discount.setScale(DISCOUNT_SCALE);
    }

    private BigDecimal calculateGroceryTotal(List<BillItem> items) {
        return items.stream()
                .filter(item -> ItemCategory.GROCERY == item.getCategory())
                .map(BillItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
