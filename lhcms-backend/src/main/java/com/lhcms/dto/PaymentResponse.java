package com.lhcms.dto;

import com.lhcms.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long appointmentId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getAppointment().getId(),
                p.getAmount(),
                p.getMethod().name(),
                p.getStatus().name(),
                p.getTransactionId(),
                p.getCreatedAt(),
                p.getCompletedAt()
        );
    }
}
