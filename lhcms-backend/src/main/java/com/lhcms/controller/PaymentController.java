package com.lhcms.controller;

import com.lhcms.dto.PaymentRequest;
import com.lhcms.dto.PaymentResponse;
import com.lhcms.security.UserPrincipal;
import com.lhcms.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.verifyPayment(id));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.getByAppointment(appointmentId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> myPayments(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(paymentService.getPatientPayments(principal.getId()));
    }
}
