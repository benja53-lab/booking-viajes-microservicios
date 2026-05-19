package com.tuequipo.ms_reservas_vuelo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Servicio cliente para comunicacion con ms-vuelos.
 * Permite verificar disponibilidad y obtener informacion de vuelos remotamente.
 */
@Service
public class VueloClientService {

    private static final Logger log = LoggerFactory.getLogger(VueloClientService.class);

    private final WebClient webClient;

    public VueloClientService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8089")
                .build();
    }

    /**
     * Consulta un vuelo por ID al ms-vuelos para verificar que existe y tiene asientos.
     *
     * @param vueloId ID del vuelo a consultar
     * @return datos del vuelo obtenidos remotamente
     */
    public Map<String, Object> buscarVueloPorId(Long vueloId) {
        log.info("Consultando vuelo {} en ms-vuelos (puerto 8089)", vueloId);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = webClient.get()
                    .uri("/api/vuelos/{id}", vueloId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            log.info("Vuelo obtenido remotamente: id={}, origen={}, destino={}",
                    resultado.get("id"), resultado.get("origen"), resultado.get("destino"));
            return resultado;
        } catch (Exception e) {
            log.error("Error al consultar ms-vuelos para vuelo {}: {}", vueloId, e.getMessage());
            throw new RuntimeException("No se pudo verificar el vuelo con ID: " + vueloId + " en ms-vuelos");
        }
    }
}
