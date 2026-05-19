# Booking de Viajes - Arquitectura de Microservicios

## Descripción
Sistema de gestión de viajes desarrollado con arquitectura de microservicios independientes usando Spring Boot, JPA/Hibernate y MySQL.

## Integrantes
- Benjamín Cisternas Estrada

## Microservicios implementados
| Microservicio | Puerto | Descripción |
|---|---|---|
| ms-reservas-vuelo | 8081 | Gestión de reservas de vuelos |
| ms-hoteles | 8082 | Gestión de hoteles y disponibilidad |
| ms-reservas-hotel | 8083 | Gestión de reservas de hotel |
| ms-pagos | 8084 | Procesamiento de pagos |
| ms-destinos | 8085 | Gestión de destinos turísticos |
| ms-paquetes | 8086 | Paquetes de viaje (vuelo + hotel) |
| ms-notificaciones | 8087 | Sistema de notificaciones |
| ms-resenas | 8088 | Reseñas y calificaciones |

## Tecnologías utilizadas
- Java 21
- Spring Boot 3.5
- Spring Data JPA + Hibernate
- MySQL (Laragon)
- Bean Validation
- SLF4J
- WebClient (comunicación entre microservicios)
- React + Vite + Tailwind CSS (Frontend)

## Funcionalidades implementadas
- CRUD completo en todos los microservicios
- Validaciones con Bean Validation
- Manejo de excepciones con @ControllerAdvice
- Logs estructurados con SLF4J
- Comunicación entre microservicios con WebClient
- Frontend React conectado a los microservicios
- CORS configurado

## Pasos para ejecutar
1. Instalar Laragon y activar MySQL
2. Abrir cada microservicio en VSCode
3. Ejecutar `./mvnw spring-boot:run` en cada uno
4. Para el frontend: `cd frontend-booking` y `npm run dev`
5. Acceder a `http://localhost:5173`