package com.example.demo.payment;

public interface SalaryPaymentStrategy {

    PaymentProviderType providerType();

    SalaryPaymentResult paySalary(SalaryPaymentCommand command);
}
