package com.alejandro.microservices.pagos.service;

import com.alejandro.microservices.pagos.dto.PaymentResponse;
import com.alejandro.microservices.pagos.model.Payment;
import com.alejandro.microservices.pagos.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<PaymentResponse> getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::mapToDto);
    }

    public PaymentResponse createPayment(Payment payment) {
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus("PENDING");
        Payment savedPayment = paymentRepository.save(payment);
        return mapToDto(savedPayment);
    }

    public PaymentResponse processPayment(Long id, String status) {
        return paymentRepository.findById(id).map(payment -> {
            payment.setStatus(status);
            payment.setProcessedAt(LocalDateTime.now());
            Payment updatedPayment = paymentRepository.save(payment);
            return mapToDto(updatedPayment);
        }).orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public PaymentResponse mapToDto(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getAmount(),
            payment.getCurrency(),
            payment.getMethod(),
            payment.getStatus(),
            payment.getCreatedAt(),
            payment.getProcessedAt()
        );
    }
}
