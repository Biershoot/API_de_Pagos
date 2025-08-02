package com.alejandro.microservices.pagos.repository;

import com.alejandro.microservices.pagos.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
