package com.tuequipo.ms_notificaciones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

/**
 * Servicio cliente para comunicacion con ms-usuarios.
 * Permite verificar que el usuario existe antes de enviar una notificacion.
 */
@Service
public class UsuarioClientService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioClientService.class);

    private final WebClient webClient;

    public UsuarioClientService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8090")
                .build();
    }

    /**
     * Consulta si un usuario existe en ms-usuarios.
     *
     * @param usuarioId ID del usuario a verificar
     * @return datos del usuario obtenidos remotamente
     */
    public Map<String, Object> buscarUsuarioPorId(Long usuarioId) {
        log.info("Verificando existencia de usuario {} en ms-usuarios (puerto 8090)", usuarioId);
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = webClient.get()
                    .uri("/api/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            log.info("Usuario verificado remotamente: id={}, email={}",
                    resultado.get("id"), resultado.get("email"));
            return resultado;
        } catch (Exception e) {
            log.error("Error al verificar usuario {} en ms-usuarios: {}", usuarioId, e.getMessage());
            throw new RuntimeException("No se pudo verificar el usuario con ID: " + usuarioId + " en ms-usuarios");
        }
    }
}
