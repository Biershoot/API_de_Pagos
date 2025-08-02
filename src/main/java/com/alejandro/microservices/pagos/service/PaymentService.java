package com.alejandro.microservices.pagos.service;

import com.alejandro.microservices.pagos.auth.User;
import com.alejandro.microservices.pagos.dto.CreatePaymentRequest;
import com.alejandro.microservices.pagos.dto.PaymentResponse;
import com.alejandro.microservices.pagos.gateway.FakePaymentGateway;
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
    private final FakePaymentGateway fakePaymentGateway;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                        FakePaymentGateway fakePaymentGateway) {
        this.paymentRepository = paymentRepository;
        this.fakePaymentGateway = fakePaymentGateway;
    }

    public List<Payment> getPaymentsByUser(User user) {
        return paymentRepository.findByUser(user);
    }

    public Optional<Payment> getPaymentByIdAndUser(Long id, User user) {
        return paymentRepository.findByIdAndUser(id, user);
    }

    public Payment createPayment(CreatePaymentRequest request, User currentUser) {
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .method(request.getMethod())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .user(currentUser)
                .build();

        return paymentRepository.save(payment);
    }

    public PaymentResponse processPayment(Long id) {
        return paymentRepository.findById(id).map(payment -> {
            boolean approved = fakePaymentGateway.processPayment(payment.getAmount(), payment.getMethod());
            payment.setStatus(approved ? "APPROVED" : "REJECTED");
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
