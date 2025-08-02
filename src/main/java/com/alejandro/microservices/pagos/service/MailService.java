package com.alejandro.microservices.pagos.service;

import com.alejandro.microservices.pagos.model.Payment;
import com.alejandro.microservices.pagos.auth.User;
import org.springframework.mail.javamail.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPaymentNotification(User user, Payment payment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Resultado de tu pago");
        message.setText("Hola " + user.getUsername() + ",\n\n"
                + "Tu pago fue procesado con el siguiente resultado:\n"
                + "Estado: " + payment.getStatus() + "\n"
                + "Monto: $" + payment.getAmount() + " " + payment.getCurrency() + "\n"
                + "Método: " + payment.getMethod() + "\n"
                + "Fecha: " + payment.getProcessedAt() + "\n\n"
                + "Gracias por usar nuestra plataforma.");

        mailSender.send(message);
    }

    public void sendPaymentCreatedNotification(User user, Payment payment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Pago creado exitosamente");
        message.setText("Hola " + user.getUsername() + ",\n\n"
                + "Hemos recibido tu solicitud de pago con los siguientes detalles:\n"
                + "ID de pago: " + payment.getId() + "\n"
                + "Monto: $" + payment.getAmount() + " " + payment.getCurrency() + "\n"
                + "Método: " + payment.getMethod() + "\n"
                + "Estado actual: " + payment.getStatus() + "\n"
                + "Fecha de creación: " + payment.getCreatedAt() + "\n\n"
                + "Te notificaremos cuando el pago sea procesado.\n"
                + "Gracias por usar nuestra plataforma.");

        mailSender.send(message);
    }

    public void sendRegistrationNotification(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Bienvenido a la API de Pagos");
        message.setText("Hola " + user.getUsername() + ",\n\n"
                + "¡Gracias por registrarte en nuestra plataforma de pagos!\n\n"
                + "Tu cuenta ha sido creada exitosamente. Ahora puedes iniciar sesión y comenzar a utilizar nuestros servicios.\n\n"
                + "Si tienes alguna pregunta, no dudes en contactarnos.\n\n"
                + "Saludos,\n"
                + "El equipo de API de Pagos");

        mailSender.send(message);
    }
}
