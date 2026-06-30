package com.tuequipo.ms_reservas_vuelo.controller;

import com.tuequipo.ms_reservas_vuelo.dto.ReservaVueloDTO;
import com.tuequipo.ms_reservas_vuelo.model.ReservaVuelo;
import com.tuequipo.ms_reservas_vuelo.service.ReservaVueloService;
import com.tuequipo.ms_reservas_vuelo.service.VueloClientService;
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

@Tag(name = "Reservas de Vuelo", description = "Gestión de reservas de vuelos realizadas por los usuarios")
@RestController
@RequestMapping("/api/reservas-vuelo")
public class ReservaVueloController {

    private static final Logger log = LoggerFactory.getLogger(ReservaVueloController.class);
    private final ReservaVueloService service;
    private final VueloClientService vueloClientService;

    public ReservaVueloController(ReservaVueloService service, VueloClientService vueloClientService) {
        this.service = service;
        this.vueloClientService = vueloClientService;
    }

    @Operation(summary = "Crear una reserva de vuelo", description = "Registra una nueva reserva de vuelo para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva de vuelo creada exitosamente",
            content = @Content(schema = @Schema(implementation = ReservaVuelo.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos")
    })
    @PostMapping
    public ResponseEntity<ReservaVuelo> crear(@Valid @RequestBody ReservaVueloDTO dto) {
        log.info("POST /api/reservas-vuelo - creando reserva para usuario {}", dto.getUsuarioId());
        ReservaVuelo creada = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @Operation(summary = "Listar todas las reservas de vuelo", description = "Retorna el listado completo de reservas de vuelo registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ReservaVuelo>> listarTodas() {
        log.info("GET /api/reservas-vuelo - listando todas");
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar reserva de vuelo por ID", description = "Retorna una reserva de vuelo específica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaVuelo> buscarPorId(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        log.info("GET /api/reservas-vuelo/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar reservas por usuario", description = "Retorna todas las reservas de vuelo realizadas por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas del usuario encontradas")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaVuelo>> buscarPorUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/reservas-vuelo/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Cambiar estado de una reserva de vuelo", description = "Actualiza el estado de una reserva de vuelo (ej: CONFIRMADA, CANCELADA, PENDIENTE)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaVuelo> cambiarEstado(
            @Parameter(description = "ID de la reserva") @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la reserva") @RequestParam ReservaVuelo.EstadoReserva nuevoEstado) {
        log.info("PUT /api/reservas-vuelo/{}/estado - nuevo estado: {}", id, nuevoEstado);
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }

    @Operation(summary = "Eliminar una reserva de vuelo", description = "Elimina una reserva de vuelo del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        log.info("DELETE /api/reservas-vuelo/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener vuelo asociado a la reserva", description = "Consulta el microservicio de vuelos para obtener el detalle del vuelo vinculado a esta reserva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vuelo obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva o vuelo no encontrados")
    })
    @GetMapping("/{id}/vuelo")
    public ResponseEntity<Map<String, Object>> obtenerVueloDeLaReserva(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        log.info("GET /api/reservas-vuelo/{}/vuelo - consultando vuelo remoto", id);
        ReservaVuelo reserva = service.buscarPorId(id);
        Map<String, Object> vuelo = vueloClientService.buscarVueloPorId(reserva.getVueloId());
        return ResponseEntity.ok(vuelo);
    }
}