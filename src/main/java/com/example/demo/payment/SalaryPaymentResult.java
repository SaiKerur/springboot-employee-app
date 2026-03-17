package com.example.demo.payment;

public record SalaryPaymentResult(
        boolean success,
        String transactionId,
        String message
) {
}
