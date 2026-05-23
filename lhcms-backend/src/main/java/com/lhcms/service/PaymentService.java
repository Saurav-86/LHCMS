package com.lhcms.service;

import com.lhcms.dto.PaymentRequest;
import com.lhcms.dto.PaymentResponse;
import com.lhcms.model.Appointment;
import com.lhcms.model.Payment;
import com.lhcms.model.enums.AppointmentStatus;
import com.lhcms.model.enums.PaymentStatus;
import com.lhcms.repository.AppointmentRepository;
import com.lhcms.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentFactory paymentFactory;

    public PaymentService(PaymentRepository paymentRepository,
                          AppointmentRepository appointmentRepository,
                          PaymentFactory paymentFactory) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentFactory = paymentFactory;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + request.getAppointmentId()));

        if (paymentRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new IllegalArgumentException("Payment already exists for this appointment.");
        }

        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());

        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse verifyPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalArgumentException("Payment already completed.");
        }

        PaymentStrategy strategy = paymentFactory.getStrategy(payment.getMethod());
        String transactionId = strategy.processPayment(
                payment.getAmount(),
                "Appointment #" + payment.getAppointment().getId()
        );

        payment.setTransactionId(transactionId);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setCompletedAt(LocalDateTime.now());

        payment.getAppointment().setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(payment.getAppointment());

        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public PaymentResponse getByAppointment(Long appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("No payment found for appointment: " + appointmentId));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPatientPayments(Long patientId) {
        return paymentRepository.findByAppointmentPatientId(patientId).stream()
                .map(PaymentResponse::from)
                .toList();
    }
}
