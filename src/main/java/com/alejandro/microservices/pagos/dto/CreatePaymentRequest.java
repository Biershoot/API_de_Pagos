package com.alejandro.microservices.pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO para crear un nuevo pago.
 *
 * Esta clase tiene todos los datos que necesitamos para crear un pago nuevo.
 * Usamos validaciones para asegurarnos de que los datos estén correctos antes
 * de procesarlos.
 *
 * @see com.alejandro.microservices.pagos.controller.PaymentController#createPayment(CreatePaymentRequest)
 */
@Data
@Schema(description = "Datos para crear un nuevo pago")
public class CreatePaymentRequest {

    /**
     * El monto del pago.
     * Tiene que ser mayor que cero.
     */
    @Schema(description = "Cantidad a pagar", example = "100.50", required = true)
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private Double amount;

    /**
     * La moneda del pago.
     * Por ejemplo: USD, EUR, COP, etc.
     */
    @Schema(description = "Tipo de moneda", example = "USD", required = true)
    @NotBlank(message = "La moneda es obligatoria")
    private String currency;

    /**
     * El método de pago.
     * Puede ser CREDIT_CARD, DEBIT_CARD, NEQUI, etc.
     */
    @Schema(description = "Forma de pago", example = "CREDIT_CARD", required = true)
    @NotBlank(message = "El método de pago es obligatorio")
    private String method;
}
