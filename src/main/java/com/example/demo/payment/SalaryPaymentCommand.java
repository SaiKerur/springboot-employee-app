package com.example.demo.payment;

import java.time.YearMonth;

public record SalaryPaymentCommand(
        Integer employeeId,
        String employeeName,
        String employeeEmail,
        double amount,
        YearMonth salaryMonth
) {
}
