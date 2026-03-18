package com.example.demo.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class StripeSalaryPaymentStrategy implements SalaryPaymentStrategy {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(ZoneOffset.UTC);

    @Value("${payment.gateway.stripe.api-key:stripe-test-key}")
    private String stripeApiKey;

    @Override
    public PaymentProviderType providerType() {
        return PaymentProviderType.STRIPE;
    }

    @Override
    public SalaryPaymentResult paySalary(SalaryPaymentCommand command) {
        String transactionId = "STRIPE-" + TIME_FORMATTER.format(Instant.now()) + "-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        String maskedKey = stripeApiKey.length() > 4
                ? "****" + stripeApiKey.substring(stripeApiKey.length() - 4)
                : "****";
        String message = "Salary paid with Stripe (key " + maskedKey + ")";
        return new SalaryPaymentResult(true, transactionId, message);
    }
}
