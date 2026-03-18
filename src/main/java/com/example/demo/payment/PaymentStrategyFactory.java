package com.example.demo.payment;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<PaymentProviderType, SalaryPaymentStrategy> strategyByProvider =
            new EnumMap<>(PaymentProviderType.class);

    public PaymentStrategyFactory(List<SalaryPaymentStrategy> strategies) {
        for (SalaryPaymentStrategy strategy : strategies) {
            strategyByProvider.put(strategy.providerType(), strategy);
        }
    }

    public SalaryPaymentStrategy getStrategy(PaymentProviderType providerType) {
        SalaryPaymentStrategy strategy = strategyByProvider.get(providerType);
        if (strategy == null) {
            throw new IllegalArgumentException("No payment strategy configured for provider: " + providerType);
        }
        return strategy;
    }
}
