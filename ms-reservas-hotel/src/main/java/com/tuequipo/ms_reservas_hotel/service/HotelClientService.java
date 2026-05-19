package com.tuequipo.ms_reservas_hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Servicio cliente para comunicacion con ms-hoteles.
 * Permite verificar disponibilidad y precio del hotel antes de confirmar una reserva.
 */
@Service
public class HotelClientService {

    private static final Logger log = LoggerFactory.getLogger(HotelClientService.class);

    private final WebClient webClient;

    public HotelClientService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    /**
     * Obtiene la informacion de un hotel desde ms-hoteles para validar
     * disponibilidad antes de crear la reserva.
     *
     * @param hotelId ID del hotel a consultar
     * @return datos del hotel obtenidos remotamente
     */
    public Map<String, Object> buscarHotelPorId(Long hotelId) {
        log.info("Consultando hotel {} en ms-hoteles (puerto 8082)", hotelId);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = webClient.get()
                    .uri("/api/hoteles/{id}", hotelId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            log.info("Hotel obtenido remotamente: id={}, nombre={}, disponible={}",
                    resultado.get("id"), resultado.get("nombre"), resultado.get("disponible"));
            return resultado;
        } catch (Exception e) {
            log.error("Error al consultar ms-hoteles para hotel {}: {}", hotelId, e.getMessage());
            throw new RuntimeException("No se pudo verificar el hotel con ID: " + hotelId + " en ms-hoteles");
        }
    }
}
