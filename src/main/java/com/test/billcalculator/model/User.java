package com.test.billcalculator.model;

import com.test.billcalculator.util.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UserType userType;
    private int userTenure;
}
