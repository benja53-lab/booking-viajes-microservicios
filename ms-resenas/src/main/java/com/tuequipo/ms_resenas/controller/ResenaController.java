package com.tuequipo.ms_resenas.controller;

import com.tuequipo.ms_resenas.dto.ResenaDTO;
import com.tuequipo.ms_resenas.model.Resena;
import com.tuequipo.ms_resenas.service.ResenaService;
import com.tuequipo.ms_resenas.service.UsuarioClientService;
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

@Tag(name = "Reseñas", description = "Gestión de reseñas de usuarios sobre hoteles y destinos")
@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private static final Logger log = LoggerFactory.getLogger(ResenaController.class);
    private final ResenaService service;
    private final UsuarioClientService usuarioClientService;

    public ResenaController(ResenaService service, UsuarioClientService usuarioClientService) {
        this.service = service;
        this.usuarioClientService = usuarioClientService;
    }

    @Operation(summary = "Crear una reseña", description = "Registra una nueva reseña de un usuario sobre un hotel o destino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la reseña inválidos")
    })
    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody ResenaDTO dto) {
        log.info("POST /api/resenas - usuario {}", dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todas las reseñas", description = "Retorna el listado completo de reseñas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Resena>> listarTodas() {
        log.info("GET /api/resenas");
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar reseña por ID", description = "Retorna una reseña específica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@Parameter(description = "ID de la reseña") @PathVariable Long id) {
        log.info("GET /api/resenas/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar reseñas por usuario", description = "Retorna todas las reseñas realizadas por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseñas del usuario encontradas")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> buscarPorUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/resenas/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar reseñas por referencia", description = "Retorna las reseñas asociadas a un elemento específico (hotel, destino, etc.) según su tipo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseñas encontradas para la referencia")
    })
    @GetMapping("/referencia/{referenciaId}")
    public ResponseEntity<List<Resena>> buscarPorReferencia(
            @Parameter(description = "ID de la referencia") @PathVariable Long referenciaId,
            @Parameter(description = "Tipo de reseña") @RequestParam Resena.TipoResena tipo) {
        log.info("GET /api/resenas/referencia/{}", referenciaId);
        return ResponseEntity.ok(service.buscarPorReferencia(referenciaId, tipo));
    }

    @Operation(summary = "Eliminar una reseña", description = "Elimina una reseña del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la reseña") @PathVariable Long id) {
        log.info("DELETE /api/resenas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener perfil del autor de la reseña", description = "Consulta el microservicio de usuarios para obtener el perfil del usuario que escribió la reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil del usuario obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuario/{usuarioId}/perfil")
    public ResponseEntity<Map<String, Object>> obtenerPerfilUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/resenas/usuario/{}/perfil - consultando ms-usuarios", usuarioId);
        Map<String, Object> usuario = usuarioClientService.verificarUsuario(usuarioId);
        return ResponseEntity.ok(usuario);
    }
}