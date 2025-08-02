package com.alejandro.microservices.pagos.model;

import com.alejandro.microservices.pagos.auth.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Clase para guardar los pagos en la base de datos.
 *
 * Aquí definimos cómo se guardan los pagos en la tabla 'payment'.
 * Usamos JPA para mapear la clase a la tabla y Lombok para
 * ahorrarnos escribir getters y setters.
 */
@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    /**
     * ID único del pago (autoincremental).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Cantidad de dinero del pago.
     */
    @Column(nullable = false)
    private Double amount;

    /**
     * Tipo de moneda (USD, EUR, COP...).
     */
    @Column(nullable = false, length = 3)
    private String currency;

    /**
     * Cómo se va a pagar (tarjeta, transferencia, etc).
     */
    @Column(nullable = false)
    private String method;

    /**
     * Estado actual del pago.
     * Puede ser: PENDING (pendiente), APPROVED (aprobado) o REJECTED (rechazado)
     */
    @Column(nullable = false)
    private String status;

    /**
     * Cuándo se creó el pago.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Cuándo se procesó el pago (cuando pasó de PENDING a otro estado).
     */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    /**
     * Usuario que hizo el pago.
     * Un usuario puede tener muchos pagos, pero cada pago es de un solo usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true) // Nullable mientras se migran los datos
    private User user;
}
