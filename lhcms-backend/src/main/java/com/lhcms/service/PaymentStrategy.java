package com.lhcms.service;

import java.math.BigDecimal;

public interface PaymentStrategy {

    String processPayment(BigDecimal amount, String description);

    String getMethodName();
}
