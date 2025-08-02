# 💳 API de Pagos | Microservicio de Procesamiento de Pagos

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-green)
![JWT](https://img.shields.io/badge/JWT-Security-blue)
![Swagger](https://img.shields.io/badge/Swagger-Documentation-green)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)

## 📋 Descripción

API RESTful para procesamiento de pagos que simula una pasarela de pago integrable con múltiples métodos de pago. Implementada con Spring Boot, seguridad JWT, notificaciones por correo electrónico y documentación Swagger.

## 🚀 Características

- **Autenticación segura** con JWT
- **Administración de pagos** con diferentes estados (PENDING, APPROVED, REJECTED)
- **Procesamiento asíncrono** mediante integración con pasarelas (simulado)
- **Seguridad por usuario** (cada usuario solo ve sus propios pagos)
- **Notificaciones por correo electrónico** en diferentes eventos
- **Documentación interactiva** con Swagger
- **Arquitectura multicapa** (controladores, servicios, repositorios)
- **Integración con base de datos** MySQL

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security** con JWT
- **Spring Data JPA**
- **Hibernate ORM**
- **MySQL**
- **Lombok**
- **Swagger/OpenAPI 3**
- **JavaMail**

## 🏗️ Arquitectura

La aplicación sigue una arquitectura en capas:

```
api-pagos/
├── controller/    # Endpoints REST y validación de entrada
├── service/       # Lógica de negocio y transacciones
├── repository/    # Acceso a datos
├── model/         # Entidades de dominio
├── dto/           # Objetos de transferencia de datos
├── auth/          # Autenticación y autorización
├── security/      # Configuración de seguridad
├── gateway/       # Integración con sistemas externos
└── config/        # Configuración de la aplicación
```

## 📊 Diagrama de Entidad-Relación

```
┌──────────┐          ┌───────────┐
│  User    │          │  Payment  │
├──────────┤          ├───────────┤
│ id       │◄─────────┤ id        │
│ username │          │ amount    │
│ email    │          │ currency  │
│ password │          │ method    │
│ role     │          │ status    │
└──────────┘          │ createdAt │
                      │ processedAt│
                      │ user_id   │
                      └───────────┘
```

## 🔗 Endpoints

### Autenticación

| Método | URL | Descripción |
|--------|-----|-------------|
| POST   | `/api/auth/register` | Registro de usuario |
| POST   | `/api/auth/login`    | Inicio de sesión y obtención de token JWT |

### Pagos

| Método | URL | Descripción |
|--------|-----|-------------|
| POST   | `/api/payments` | Crear un nuevo pago |
| GET    | `/api/payments` | Obtener lista de pagos del usuario |
| GET    | `/api/payments/{id}` | Obtener detalles de un pago específico |
| POST   | `/api/payments/{id}/process` | Procesar un pago (aprobar/rechazar) |
| DELETE | `/api/payments/{id}` | Eliminar un pago |

## ⚙️ Configuración y Ejecución

### Requisitos

- Java 17+
- MySQL 8+
- Maven

### Configuración

1. Clona este repositorio
2. Configura tu base de datos en `application.properties`
3. Configura el servidor SMTP para notificaciones por correo

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/api_pagos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu_correo@gmail.com
spring.mail.password=tu_contraseña_app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Ejecución

```bash
mvn clean install
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`
Documentación Swagger: `http://localhost:8080/swagger-ui/index.html`

## 📱 Prueba con Postman

1. Registra un usuario:
   ```
   POST http://localhost:8080/api/auth/register
   {
     "username": "usuario",
     "email": "usuario@example.com",
     "password": "contraseña"
   }
   ```

2. Inicia sesión para obtener el token JWT:
   ```
   POST http://localhost:8080/api/auth/login
   {
     "username": "usuario", 
     "password": "contraseña"
   }
   ```

3. Crea un pago (usando el token JWT en el header):
   ```
   POST http://localhost:8080/api/payments
   Authorization: Bearer {tu_token_jwt}
   {
     "amount": 100.50,
     "currency": "USD", 
     "method": "CREDIT_CARD"
   }
   ```

## 🔍 Características Destacadas

### 1. Seguridad JWT

```java
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    String token = userService.authenticate(request);
    return ResponseEntity.ok(new AuthResponse(token));
}
```

### 2. Procesamiento de Pagos

```java
@PostMapping("/{id}/process")
public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id) {
    PaymentResponse processedPayment = paymentService.processPayment(id);
    return ResponseEntity.ok(processedPayment);
}
```

### 3. Notificaciones por Correo

```java
public void sendPaymentNotification(User user, Payment payment) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(user.getEmail());
    message.setSubject("Resultado de tu pago");
    message.setText("Tu pago por $" + payment.getAmount() + 
                   " ha sido " + payment.getStatus());
    mailSender.send(message);
}
```

## 🔮 Futuras Mejoras

- Implementar pasarelas de pago reales (Stripe, PayPal)
- Añadir soporte para pagos recurrentes
- Implementar reportes y estadísticas
- Desarrollar una interfaz de usuario con React/Angular

## 👨‍💻 Autor

Alejandro - [GitHub](https://github.com/Biershoot)
