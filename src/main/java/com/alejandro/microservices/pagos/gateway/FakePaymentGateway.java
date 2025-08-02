package com.alejandro.microservices.pagos.gateway;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FakePaymentGateway {

    private final Random random = new Random();

    public boolean processPayment(double amount, String method) {
        // Simula un pequeño retraso en la respuesta
        try {
            Thread.sleep(1000); // 1 segundo
        } catch (InterruptedException ignored) {}

        // Simulación aleatoria (70% aprobado, 30% rechazado)
        return random.nextDouble() < 0.7;
    }
}
