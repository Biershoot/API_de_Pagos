package com.alejandro.microservices.pagos.controller;

import com.alejandro.microservices.pagos.auth.User;
import com.alejandro.microservices.pagos.dto.CreatePaymentRequest;
import com.alejandro.microservices.pagos.dto.PaymentResponse;
import com.alejandro.microservices.pagos.model.Payment;
import com.alejandro.microservices.pagos.security.CurrentUserProvider;
import com.alejandro.microservices.pagos.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controlador para manejar todo lo relacionado con pagos.
 *
 * Aquí están todos los endpoints que usamos para crear, ver, procesar
 * y borrar pagos. Todos necesitan que el usuario esté logueado (JWT).
 */
@RestController
@RequestMapping("/api/payments")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pagos", description = "Endpoints de pagos")
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public PaymentController(PaymentService paymentService, CurrentUserProvider currentUserProvider) {
        this.paymentService = paymentService;
        this.currentUserProvider = currentUserProvider;
    }

    /**
     * Obtiene todos los pagos del usuario logueado.
     */
    @Operation(summary = "Ver mis pagos", description = "Muestra todos los pagos del usuario logueado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pagos"),
        @ApiResponse(responseCode = "401", description = "No has iniciado sesión", content = @Content)
    })
    @GetMapping
    public List<PaymentResponse> getMyPayments() {
        User currentUser = currentUserProvider.getCurrentUser();
        return paymentService.getPaymentsByUser(currentUser)
                .stream()
                .map(paymentService::mapToDto)
                .toList();
    }

    /**
     * Busca un pago específico por ID.
     * Solo funciona si el pago pertenece al usuario logueado.
     */
    @Operation(summary = "Buscar un pago", description = "Busca un pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "401", description = "No has iniciado sesión", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @Parameter(description = "ID del pago") @PathVariable Long id) {
        try {
            User currentUser = currentUserProvider.getCurrentUser();
            Payment payment = paymentService.getPaymentByIdAndUser(id, currentUser)
                    .orElseThrow(() -> new NoSuchElementException("Pago no encontrado"));
            return ResponseEntity.ok(paymentService.mapToDto(payment));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo pago.
     */
    @Operation(summary = "Crear pago", description = "Crea un nuevo pago")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado"),
        @ApiResponse(responseCode = "400", description = "Datos incorrectos"),
        @ApiResponse(responseCode = "401", description = "No has iniciado sesión", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid CreatePaymentRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();
        Payment payment = paymentService.createPayment(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.mapToDto(payment));
    }

    /**
     * Procesa un pago (lo aprueba o rechaza).
     */
    @Operation(summary = "Procesar pago", description = "Cambia el estado de un pago a APPROVED o REJECTED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago procesado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "401", description = "No has iniciado sesión", content = @Content)
    })
    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id) {
        try {
            PaymentResponse processedPayment = paymentService.processPayment(id);
            return ResponseEntity.ok(processedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Borra un pago.
     */
    @Operation(summary = "Borrar pago", description = "Elimina un pago existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago borrado"),
        @ApiResponse(responseCode = "401", description = "No has iniciado sesión", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
