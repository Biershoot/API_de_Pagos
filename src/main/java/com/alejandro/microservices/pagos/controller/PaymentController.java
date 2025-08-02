package com.alejandro.microservices.pagos.controller;

import com.alejandro.microservices.pagos.dto.CreatePaymentRequest;
import com.alejandro.microservices.pagos.dto.PaymentResponse;
import com.alejandro.microservices.pagos.model.Payment;
import com.alejandro.microservices.pagos.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        try {
            PaymentResponse payment = paymentService.getPaymentById(id)
                    .orElseThrow();
            return ResponseEntity.ok(payment);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid CreatePaymentRequest request) {
        Payment payment = Payment.builder()
            .amount(request.getAmount())
            .currency(request.getCurrency())
            .method(request.getMethod())
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .build();

        PaymentResponse createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id, @RequestParam boolean approved) {
        try {
            String status = approved ? "APPROVED" : "REJECTED";
            PaymentResponse processedPayment = paymentService.processPayment(id, status);
            return ResponseEntity.ok(processedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
