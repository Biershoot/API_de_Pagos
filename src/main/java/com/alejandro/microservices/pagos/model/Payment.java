package com.alejandro.microservices.pagos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private String currency;

    private String method; // Ej: "CARD", "NEQUI", "PSE"

    private String status; // Ej: "PENDING", "APPROVED", "REJECTED"

    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
}

