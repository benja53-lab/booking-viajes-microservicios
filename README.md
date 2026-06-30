# Booking de Viajes - Arquitectura de Microservicios

## Descripción
Sistema integral de gestión de viajes desarrollado con arquitectura de microservicios independientes usando Spring Boot 3.5, JPA/Hibernate y MySQL. Cada microservicio posee su propia base de datos, lógica de negocio y se comunica con otros servicios a través de WebClient.

## Integrantes
- Benjamín Cisternas Estrada
- Felipe Cartes

## Microservicios implementados

| Microservicio       | Puerto | Descripción                                              |
|---------------------|--------|----------------------------------------------------------|
| ms-reservas-vuelo   | 8081   | Gestión de reservas de vuelos (consume ms-vuelos)        |
| ms-hoteles          | 8082   | Gestión de hoteles y disponibilidad                      |
| ms-reservas-hotel   | 8083   | Gestión de reservas de hotel (consume ms-hoteles)        |
| ms-pagos            | 8084   | Procesamiento de pagos (consume ms-reservas-*)           |
| ms-destinos         | 8085   | Gestión de destinos turísticos                           |
| ms-paquetes         | 8086   | Paquetes de viaje - vuelo + hotel (consume ms-hoteles)   |
| ms-notificaciones   | 8087   | Sistema de notificaciones (consume ms-usuarios)          |
| ms-resenas          | 8088   | Reseñas y calificaciones (consume ms-usuarios)           |
| ms-vuelos           | 8089   | Gestión de vuelos y disponibilidad                       |
| ms-usuarios         | 8090   | Gestión de usuarios registrados                          |
| ms-gateway          | 9090   | API Gateway - centraliza el enrutamiento a los 10 servicios |

## Tecnologías utilizadas
- Java 21
- Spring Boot 3.5
- Spring Cloud Gateway (API Gateway)
- Spring Data JPA + Hibernate
- MySQL (Laragon)
- Bean Validation (JSR 380)
- SLF4J (logs estructurados)
- WebClient / Spring WebFlux (comunicación entre microservicios)
- JUnit 5 + Mockito (pruebas unitarias)
- SpringDoc OpenAPI / Swagger UI (documentación técnica)

## Funcionalidades implementadas
- CRUD completo en los 10 microservicios
- Persistencia con JPA + Hibernate (base de datos separada por microservicio)
- Validaciones con Bean Validation en DTOs
- Manejo centralizado de excepciones con `@ControllerAdvice`
- Logs estructurados con SLF4J en todas las capas
- Comunicación entre microservicios con WebClient
- Patrón CSR (Controller - Service - Repository) en todos los microservicios
- Respuestas REST con `ResponseEntity` y códigos HTTP correctos
- Reglas de negocio aplicadas en la capa de servicio
- API Gateway centralizando rutas hacia los 10 microservicios
- Pruebas unitarias con JUnit 5 y Mockito
- Documentación técnica con Swagger/OpenAPI en todos los microservicios

## API Gateway - Rutas principales

El Gateway centraliza todas las peticiones en el puerto **9090**.

| Ruta Gateway                        | Redirige a                        |
|-------------------------------------|-----------------------------------|
| `GET /api/usuarios/**`              | ms-usuarios (puerto 8090)         |
| `GET /api/hoteles/**`               | ms-hoteles (puerto 8082)          |
| `GET /api/vuelos/**`                | ms-vuelos (puerto 8089)           |
| `GET /api/destinos/**`              | ms-destinos (puerto 8085)         |
| `GET /api/paquetes/**`              | ms-paquetes (puerto 8086)         |
| `GET /api/reservas-hotel/**`        | ms-reservas-hotel (puerto 8083)   |
| `GET /api/reservas-vuelo/**`        | ms-reservas-vuelo (puerto 8081)   |
| `GET /api/pagos/**`                 | ms-pagos (puerto 8084)            |
| `GET /api/resenas/**`               | ms-resenas (puerto 8088)          |
| `GET /api/notificaciones/**`        | ms-notificaciones (puerto 8087)   |

## Documentación Swagger/OpenAPI

Con cada microservicio corriendo, la documentación está disponible en:

| Microservicio       | URL Swagger                                      |
|---------------------|--------------------------------------------------|
| ms-usuarios         | http://localhost:8090/swagger-ui.html            |
| ms-hoteles          | http://localhost:8082/swagger-ui.html            |
| ms-vuelos           | http://localhost:8089/swagger-ui.html            |
| ms-destinos         | http://localhost:8085/swagger-ui.html            |
| ms-paquetes         | http://localhost:8086/swagger-ui.html            |
| ms-reservas-hotel   | http://localhost:8083/swagger-ui.html            |
| ms-reservas-vuelo   | http://localhost:8081/swagger-ui.html            |
| ms-pagos            | http://localhost:8084/swagger-ui.html            |
| ms-resenas          | http://localhost:8088/swagger-ui.html            |
| ms-notificaciones   | http://localhost:8087/swagger-ui.html            |

## Pruebas unitarias

Ejecutar desde cada microservicio:

```bash
cd ms-usuarios && ./mvnw test
```

Las pruebas validan las reglas de negocio del servicio usando JUnit 5 y Mockito, sin necesidad de conexión a base de datos.

## Comunicaciones entre microservicios
- **ms-reservas-vuelo** → consulta ms-vuelos para verificar disponibilidad de asientos
- **ms-reservas-hotel** → consulta ms-hoteles para verificar disponibilidad del hotel
- **ms-pagos** → consulta ms-reservas-hotel / ms-reservas-vuelo para validar la reserva
- **ms-paquetes** → consulta ms-hoteles para obtener información del hotel del paquete
- **ms-notificaciones** → consulta ms-usuarios para verificar destinatario
- **ms-resenas** → consulta ms-usuarios para verificar autor de la reseña

## Pasos para ejecutar localmente

### Requisitos previos
1. Instalar Laragon (incluye MySQL y Java)
2. Tener Java 21 instalado
3. Activar MySQL en Laragon (puerto 3306)

### Ejecución
Abrir una terminal por cada microservicio y ejecutar en orden:

```bash
# 1. Primero los microservicios base (sin dependencias remotas)
cd ms-usuarios && ./mvnw spring-boot:run       # Puerto 8090
cd ms-hoteles && ./mvnw spring-boot:run        # Puerto 8082
cd ms-vuelos && ./mvnw spring-boot:run         # Puerto 8089
cd ms-destinos && ./mvnw spring-boot:run       # Puerto 8085

# 2. Luego los que dependen de los anteriores
cd ms-reservas-vuelo && ./mvnw spring-boot:run # Puerto 8081
cd ms-reservas-hotel && ./mvnw spring-boot:run # Puerto 8083
cd ms-paquetes && ./mvnw spring-boot:run       # Puerto 8086

# 3. Finalmente los de nivel superior
cd ms-pagos && ./mvnw spring-boot:run          # Puerto 8084
cd ms-notificaciones && ./mvnw spring-boot:run # Puerto 8087
cd ms-resenas && ./mvnw spring-boot:run        # Puerto 8088

# 4. API Gateway (puede levantarse en cualquier momento)
cd ms-gateway && ./mvnw spring-boot:run        # Puerto 9090
```

Las bases de datos se crean automáticamente con `createDatabaseIfNotExist=true`.

## Pruebas con Postman

### Directo a cada microservicio