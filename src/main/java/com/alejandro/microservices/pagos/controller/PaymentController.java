package com.alejandro.microservices.pagos.controller;

import com.alejandro.microservices.pagos.auth.User;
import com.alejandro.microservices.pagos.dto.CreatePaymentRequest;
import com.alejandro.microservices.pagos.dto.PaymentResponse;
import com.alejandro.microservices.pagos.model.Payment;
import com.alejandro.microservices.pagos.security.CurrentUserProvider;
import com.alejandro.microservices.pagos.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/payments")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public PaymentController(PaymentService paymentService, CurrentUserProvider currentUserProvider) {
        this.paymentService = paymentService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public List<PaymentResponse> getMyPayments() {
        User currentUser = currentUserProvider.getCurrentUser();
        return paymentService.getPaymentsByUser(currentUser)
                .stream()
                .map(paymentService::mapToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        try {
            User currentUser = currentUserProvider.getCurrentUser();
            Payment payment = paymentService.getPaymentByIdAndUser(id, currentUser)
                    .orElseThrow(() -> new NoSuchElementException("Pago no encontrado"));
            return ResponseEntity.ok(paymentService.mapToDto(payment));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid CreatePaymentRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();
        Payment payment = paymentService.createPayment(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.mapToDto(payment));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id) {
        try {
            PaymentResponse processedPayment = paymentService.processPayment(id);
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
