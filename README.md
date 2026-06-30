# Booking de Viajes - Arquitectura de Microservicios

## Descripción del proyecto
Sistema integral de gestión de viajes desarrollado con arquitectura de microservicios independientes usando Spring Boot 3.5, JPA/Hibernate y MySQL. Cada microservicio posee su propia base de datos, lógica de negocio y se comunica con otros servicios a través de WebClient REST.

## Integrantes del equipo
- Benjamín Cisternas Estrada
- Felipe Lareencarnacion

## Microservicios implementados

| Microservicio       | Puerto | Descripción                                              |
|---------------------|--------|----------------------------------------------------------|
| ms-eureka-server    | 8761   | Servidor de descubrimiento de servicios (Eureka)         |
| ms-gateway          | 9090   | API Gateway - centraliza el enrutamiento a los 10 servicios |
| ms-usuarios         | 8090   | Gestión de usuarios registrados                          |
| ms-hoteles          | 8082   | Gestión de hoteles y disponibilidad                      |
| ms-vuelos           | 8089   | Gestión de vuelos y disponibilidad                       |
| ms-destinos         | 8085   | Gestión de destinos turísticos                           |
| ms-paquetes         | 8086   | Paquetes de viaje - vuelo + hotel (consume ms-hoteles)   |
| ms-reservas-hotel   | 8083   | Gestión de reservas de hotel (consume ms-hoteles)        |
| ms-reservas-vuelo   | 8081   | Gestión de reservas de vuelos (consume ms-vuelos)        |
| ms-pagos            | 8084   | Procesamiento de pagos (consume ms-reservas-*)           |
| ms-resenas          | 8088   | Reseñas y calificaciones (consume ms-usuarios)           |
| ms-notificaciones   | 8087   | Sistema de notificaciones (consume ms-usuarios)          |

## Tecnologías utilizadas
- Java 21
- Spring Boot 3.5
- Spring Cloud Gateway (API Gateway)
- Spring Cloud Netflix Eureka (descubrimiento de servicios)
- Spring Data JPA + Hibernate
- MySQL 8
- Docker + Docker Compose
- Bean Validation (JSR 380)
- SLF4J (logs estructurados)
- WebClient / Spring WebFlux (comunicación entre microservicios)
- JUnit 5 + Mockito (pruebas unitarias)
- SpringDoc OpenAPI / Swagger UI (documentación técnica)

## API Gateway - Rutas principales

El Gateway centraliza todas las peticiones en el puerto **9090**.

| Ruta Gateway                 | Redirige a                        |
|------------------------------|-----------------------------------|
| `/api/usuarios/**`           | ms-usuarios (puerto 8090)         |
| `/api/hoteles/**`            | ms-hoteles (puerto 8082)          |
| `/api/vuelos/**`             | ms-vuelos (puerto 8089)           |
| `/api/destinos/**`           | ms-destinos (puerto 8085)         |
| `/api/paquetes/**`           | ms-paquetes (puerto 8086)         |
| `/api/reservas-hotel/**`     | ms-reservas-hotel (puerto 8083)   |
| `/api/reservas-vuelo/**`     | ms-reservas-vuelo (puerto 8081)   |
| `/api/pagos/**`              | ms-pagos (puerto 8084)            |
| `/api/resenas/**`            | ms-resenas (puerto 8088)          |
| `/api/notificaciones/**`     | ms-notificaciones (puerto 8087)   |

## Documentación Swagger/OpenAPI

### Local
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

### Remoto (Render)
| Servicio            | URL                                                        |
|---------------------|------------------------------------------------------------|
| Eureka Server       | https://booking-viajes-microservicios.onrender.com         |
| API Gateway         | https://ms-gateway-x2ia.onrender.com                       |

## Despliegue remoto (Render - PaaS)
El proyecto está desplegado en Render con los siguientes servicios:
- **Eureka Server**: https://booking-viajes-microservicios.onrender.com
- **API Gateway**: https://ms-gateway-x2ia.onrender.com

## Ejecución local con Docker

### Requisitos
- Docker Desktop instalado y corriendo

### Comando
```bash
docker-compose up --build
```

Esto levanta automáticamente:
- MySQL (puerto 3307)
- Eureka Server (puerto 8761)
- API Gateway (puerto 9090)
- Los 10 microservicios (puertos 8081-8090)

### Verificar que todo está corriendo
Abrir en el navegador: http://localhost:8761

Deben aparecer los 10 microservicios registrados como UP.

## Ejecución local sin Docker

### Requisitos
1. Java 21 instalado
2. Laragon con MySQL corriendo (puerto 3306)

### Orden de ejecución
```bash
# 1. Eureka Server
cd ms-eureka-server && ./mvnw spring-boot:run

# 2. Microservicios base
cd ms-usuarios && ./mvnw spring-boot:run
cd ms-hoteles && ./mvnw spring-boot:run
cd ms-vuelos && ./mvnw spring-boot:run
cd ms-destinos && ./mvnw spring-boot:run

# 3. Microservicios dependientes
cd ms-reservas-vuelo && ./mvnw spring-boot:run
cd ms-reservas-hotel && ./mvnw spring-boot:run
cd ms-paquetes && ./mvnw spring-boot:run
cd ms-pagos && ./mvnw spring-boot:run
cd ms-notificaciones && ./mvnw spring-boot:run
cd ms-resenas && ./mvnw spring-boot:run

# 4. API Gateway
cd ms-gateway && ./mvnw spring-boot:run
```

## Pruebas unitarias

Ejecutar desde la carpeta del microservicio:
```bash
cd ms-usuarios
./mvnw test
```

Las pruebas validan reglas de negocio con JUnit 5 y Mockito, sin conexión a base de datos. Resultado esperado: **BUILD SUCCESS - 6 tests passed**.

## Comunicación entre microservicios
- **ms-reservas-vuelo** → consulta ms-vuelos para verificar asientos disponibles
- **ms-reservas-hotel** → consulta ms-hoteles para verificar disponibilidad
- **ms-pagos** → consulta ms-reservas-hotel / ms-reservas-vuelo para validar reserva
- **ms-paquetes** → consulta ms-hoteles para información del hotel
- **ms-notificaciones** → consulta ms-usuarios para verificar destinatario
- **ms-resenas** → consulta ms-usuarios para verificar autor

## Repositorio GitHub
https://github.com/benja53-lab/booking-viajes-microservicios