package com.alejandro.microservices.pagos.repository;

import com.alejandro.microservices.pagos.auth.User;
import com.alejandro.microservices.pagos.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Encontrar todos los pagos de un usuario específico
    List<Payment> findByUser(User user);

    // Encontrar un pago específico de un usuario específico
    Optional<Payment> findByIdAndUser(Long id, User user);
}
