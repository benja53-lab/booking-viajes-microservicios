package com.tuequipo.ms_pagos.service;

import com.tuequipo.ms_pagos.model.Pago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Servicio cliente para comunicacion con ms-reservas-hotel y ms-reservas-vuelo.
 * Verifica que la reserva asociada al pago exista y este en estado valido.
 */
@Service
public class ReservaClientService {

    private static final Logger log = LoggerFactory.getLogger(ReservaClientService.class);

    private final WebClient hotelWebClient;
    private final WebClient vueloWebClient;

    public ReservaClientService(WebClient.Builder builder) {
        this.hotelWebClient = builder.baseUrl("http://localhost:8083").build();
        this.vueloWebClient = builder.baseUrl("http://localhost:8081").build();
    }

    /**
     * Verifica que la reserva (hotel o vuelo) exista antes de procesar el pago.
     *
     * @param reservaId ID de la reserva
     * @param tipo tipo de reserva (HOTEL, VUELO, PAQUETE)
     * @return datos de la reserva obtenidos remotamente
     */
    public Map<String, Object> verificarReserva(Long reservaId, Pago.TipoReserva tipo) {
        log.info("Verificando reserva {} de tipo {} antes de procesar pago", reservaId, tipo);
        try {
            WebClient client;
            String uri;

            if (tipo == Pago.TipoReserva.HOTEL || tipo == Pago.TipoReserva.PAQUETE) {
                client = hotelWebClient;
                uri = "/api/reservas-hotel/" + reservaId;
                log.info("Consultando reserva de hotel {} en ms-reservas-hotel (puerto 8083)", reservaId);
            } else {
                client = vueloWebClient;
                uri = "/api/reservas-vuelo/" + reservaId;
                log.info("Consultando reserva de vuelo {} en ms-reservas-vuelo (puerto 8081)", reservaId);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = client.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            log.info("Reserva verificada: id={}, estado={}", resultado.get("id"), resultado.get("estado"));
            return resultado;
        } catch (Exception e) {
            log.error("Error al verificar reserva {} tipo {}: {}", reservaId, tipo, e.getMessage());
            throw new RuntimeException("No se pudo verificar la reserva con ID: " + reservaId);
        }
    }
}
