package com.tuequipo.ms_notificaciones.controller;

import com.tuequipo.ms_notificaciones.dto.NotificacionDTO;
import com.tuequipo.ms_notificaciones.model.Notificacion;
import com.tuequipo.ms_notificaciones.service.NotificacionService;
import com.tuequipo.ms_notificaciones.service.UsuarioClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);
    private final NotificacionService service;
    private final UsuarioClientService usuarioClientService;

    public NotificacionController(NotificacionService service, UsuarioClientService usuarioClientService) {
        this.service = service;
        this.usuarioClientService = usuarioClientService;
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody NotificacionDTO dto) {
        log.info("POST /api/notificaciones - usuario {}", dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listarTodas() {
        log.info("GET /api/notificaciones");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/notificaciones/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> buscarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> buscarNoLeidas(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/no-leidas", usuarioId);
        return ResponseEntity.ok(service.buscarNoLeidas(usuarioId));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id) {
        log.info("PUT /api/notificaciones/{}/leer", id);
        return ResponseEntity.ok(service.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Comunicacion entre microservicios — verifica usuario en ms-usuarios
    @GetMapping("/usuario/{usuarioId}/info")
    public ResponseEntity<Map<String, Object>> obtenerInfoUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/info - consultando ms-usuarios", usuarioId);
        Map<String, Object> usuario = usuarioClientService.buscarUsuarioPorId(usuarioId);
        return ResponseEntity.ok(usuario);
    }
}
