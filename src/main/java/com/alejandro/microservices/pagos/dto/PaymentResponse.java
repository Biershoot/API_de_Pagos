package com.alejandro.microservices.pagos.dto;

import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Double amount;
    private String currency;
    private String method;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    // Constructor
    public PaymentResponse(Long id, Double amount, String currency, String method,
                           String status, LocalDateTime createdAt, LocalDateTime processedAt) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    // Getters
    public Long getId() { return id; }
    public Double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}
