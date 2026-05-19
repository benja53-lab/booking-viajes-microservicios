package com.tuequipo.ms_paquetes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class HotelClientService {

    private static final Logger log = LoggerFactory.getLogger(HotelClientService.class);

    private final WebClient webClient;

    public HotelClientService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    // Consulta un hotel por ID al ms-hoteles
    public Map buscarHotelPorId(Long hotelId) {
        log.info("Consultando hotel {} en ms-hoteles", hotelId);
        try {
            Map resultado = webClient.get()
                    .uri("/api/hoteles/{id}", hotelId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            log.info("Hotel encontrado: {}", resultado);
            return resultado;
        } catch (Exception e) {
            log.error("Error al consultar hotel {}: {}", hotelId, e.getMessage());
            throw new RuntimeException("No se pudo obtener el hotel con ID: " + hotelId);
        }
    }
}