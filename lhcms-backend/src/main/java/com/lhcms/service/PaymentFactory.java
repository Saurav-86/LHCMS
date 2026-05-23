package com.lhcms.service;

import com.lhcms.model.enums.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {

    private final EsewaPaymentStrategy esewaStrategy;
    private final KhaltiPaymentStrategy khaltiStrategy;

    public PaymentFactory(EsewaPaymentStrategy esewaStrategy, KhaltiPaymentStrategy khaltiStrategy) {
        this.esewaStrategy = esewaStrategy;
        this.khaltiStrategy = khaltiStrategy;
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        return switch (method) {
            case ESEWA -> esewaStrategy;
            case KHALTI -> khaltiStrategy;
        };
    }
}
