package com.lhcms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EsewaPaymentStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(EsewaPaymentStrategy.class);

    @Override
    public String processPayment(BigDecimal amount, String description) {
        log.info("Processing eSewa payment: NPR {} for {}", amount, description);
        // Simulated eSewa gateway call — replace with real eSewa API integration
        return "ESEWA-" + System.currentTimeMillis();
    }

    @Override
    public String getMethodName() {
        return "ESEWA";
    }
}
