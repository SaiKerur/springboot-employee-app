package com.example.demo.payment;

public enum PaymentProviderType {
    STRIPE,
    RAZORPAY,
    NEFT;

    public static PaymentProviderType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment provider cannot be empty");
        }
        try {
            return PaymentProviderType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unsupported payment provider: " + value);
        }
    }
}
