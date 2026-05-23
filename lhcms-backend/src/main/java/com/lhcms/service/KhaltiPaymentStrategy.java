package com.lhcms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class KhaltiPaymentStrategy implements PaymentStrategy {

    private static final Logger log = LoggerFactory.getLogger(KhaltiPaymentStrategy.class);

    @Override
    public String processPayment(BigDecimal amount, String description) {
        log.info("Processing Khalti payment: NPR {} for {}", amount, description);
        // Simulated Khalti gateway call — replace with real Khalti API integration
        return "KHALTI-" + System.currentTimeMillis();
    }

    @Override
    public String getMethodName() {
        return "KHALTI";
    }
}
