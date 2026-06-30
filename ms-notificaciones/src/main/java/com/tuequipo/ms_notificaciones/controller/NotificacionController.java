package com.tuequipo.ms_notificaciones.controller;

import com.tuequipo.ms_notificaciones.dto.NotificacionDTO;
import com.tuequipo.ms_notificaciones.model.Notificacion;
import com.tuequipo.ms_notificaciones.service.NotificacionService;
import com.tuequipo.ms_notificaciones.service.UsuarioClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Tag(name = "Notificaciones", description = "Gestión de notificaciones enviadas a los usuarios del sistema")
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

    @Operation(summary = "Crear una notificación", description = "Registra una nueva notificación para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente",
            content = @Content(schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la notificación inválidos")
    })
    @PostMapping
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody NotificacionDTO dto) {
        log.info("POST /api/notificaciones - usuario {}", dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todas las notificaciones", description = "Retorna el listado completo de notificaciones registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Notificacion>> listarTodas() {
        log.info("GET /api/notificaciones");
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar notificación por ID", description = "Retorna una notificación específica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@Parameter(description = "ID de la notificación") @PathVariable Long id) {
        log.info("GET /api/notificaciones/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar notificaciones por usuario", description = "Retorna todas las notificaciones de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones del usuario encontradas")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> buscarPorUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar notificaciones no leídas", description = "Retorna las notificaciones no leídas de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones no leídas encontradas")
    })
    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> buscarNoLeidas(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/no-leidas", usuarioId);
        return ResponseEntity.ok(service.buscarNoLeidas(usuarioId));
    }

    @Operation(summary = "Marcar notificación como leída", description = "Actualiza el estado de una notificación a leída")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@Parameter(description = "ID de la notificación") @PathVariable Long id) {
        log.info("PUT /api/notificaciones/{}/leer", id);
        return ResponseEntity.ok(service.marcarComoLeida(id));
    }

    @Operation(summary = "Eliminar una notificación", description = "Elimina una notificación del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la notificación") @PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener información del usuario", description = "Consulta el microservicio de usuarios para obtener el detalle del usuario asociado a las notificaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información del usuario obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{usuarioId}/info")
    public ResponseEntity<Map<String, Object>> obtenerInfoUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/info - consultando ms-usuarios", usuarioId);
        Map<String, Object> usuario = usuarioClientService.buscarUsuarioPorId(usuarioId);
        return ResponseEntity.ok(usuario);
    }
}