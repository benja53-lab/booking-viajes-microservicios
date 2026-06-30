package com.tuequipo.ms_pagos.controller;

import com.tuequipo.ms_pagos.dto.PagoDTO;
import com.tuequipo.ms_pagos.model.Pago;
import com.tuequipo.ms_pagos.service.PagoService;
import com.tuequipo.ms_pagos.service.ReservaClientService;
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

@Tag(name = "Pagos", description = "Gestión de pagos asociados a reservas de hoteles y vuelos")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);
    private final PagoService service;
    private final ReservaClientService reservaClientService;

    public PagoController(PagoService service, ReservaClientService reservaClientService) {
        this.service = service;
        this.reservaClientService = reservaClientService;
    }

    @Operation(summary = "Registrar un pago", description = "Crea un nuevo pago asociado a una reserva de hotel o vuelo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente",
            content = @Content(schema = @Schema(implementation = Pago.class))),
        @ApiResponse(responseCode = "400", description = "Datos del pago inválidos")
    })
    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        log.info("POST /api/pagos - reserva {} tipo {}", dto.getReservaId(), dto.getTipoReserva());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todos los pagos", description = "Retorna el listado completo de pagos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        log.info("GET /api/pagos");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar pago por ID", description = "Retorna un pago específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@Parameter(description = "ID del pago") @PathVariable Long id) {
        log.info("GET /api/pagos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar pagos por usuario", description = "Retorna todos los pagos realizados por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagos del usuario encontrados")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> buscarPorUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/pagos/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Cambiar estado de un pago", description = "Actualiza el estado de un pago (ej: PENDIENTE, COMPLETADO, RECHAZADO)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> cambiarEstado(
            @Parameter(description = "ID del pago") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pago") @RequestParam Pago.EstadoPago nuevoEstado) {
        log.info("PUT /api/pagos/{}/estado -> {}", id, nuevoEstado);
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }

    @Operation(summary = "Eliminar un pago", description = "Elimina un pago del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del pago") @PathVariable Long id) {
        log.info("DELETE /api/pagos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener reserva asociada a un pago", description = "Consulta el microservicio de reservas para obtener el detalle de la reserva vinculada a este pago")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago o reserva no encontrados")
    })
    @GetMapping("/{id}/reserva")
    public ResponseEntity<Map<String, Object>> obtenerReservaDePago(@Parameter(description = "ID del pago") @PathVariable Long id) {
        log.info("GET /api/pagos/{}/reserva - consultando microservicio de reservas", id);
        Pago pago = service.buscarPorId(id);
        Map<String, Object> reserva = reservaClientService.verificarReserva(
                pago.getReservaId(), pago.getTipoReserva());
        return ResponseEntity.ok(reserva);
    }
}   