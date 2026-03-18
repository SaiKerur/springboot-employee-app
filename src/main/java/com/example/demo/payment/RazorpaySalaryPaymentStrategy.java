package com.example.demo.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class RazorpaySalaryPaymentStrategy implements SalaryPaymentStrategy {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(ZoneOffset.UTC);

    @Value("${payment.gateway.razorpay.api-key:razorpay-test-key}")
    private String razorpayApiKey;

    @Override
    public PaymentProviderType providerType() {
        return PaymentProviderType.RAZORPAY;
    }

    @Override
    public SalaryPaymentResult paySalary(SalaryPaymentCommand command) {
        String transactionId = "RZP-" + TIME_FORMATTER.format(Instant.now()) + "-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        String maskedKey = razorpayApiKey.length() > 4
                ? "****" + razorpayApiKey.substring(razorpayApiKey.length() - 4)
                : "****";
        String message = "Salary paid with Razorpay (key " + maskedKey + ")";
        return new SalaryPaymentResult(true, transactionId, message);
    }
}
