package com.tuequipo.ms_reservas_hotel.controller;

import com.tuequipo.ms_reservas_hotel.dto.ReservaHotelDTO;
import com.tuequipo.ms_reservas_hotel.model.ReservaHotel;
import com.tuequipo.ms_reservas_hotel.service.HotelClientService;
import com.tuequipo.ms_reservas_hotel.service.ReservaHotelService;
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

@Tag(name = "Reservas de Hotel", description = "Gestión de reservas de hoteles realizadas por los usuarios")
@RestController
@RequestMapping("/api/reservas-hotel")
public class ReservaHotelController {

    private static final Logger log = LoggerFactory.getLogger(ReservaHotelController.class);
    private final ReservaHotelService service;
    private final HotelClientService hotelClientService;

    public ReservaHotelController(ReservaHotelService service, HotelClientService hotelClientService) {
        this.service = service;
        this.hotelClientService = hotelClientService;
    }

    @Operation(summary = "Crear una reserva de hotel", description = "Registra una nueva reserva de hotel para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente",
            content = @Content(schema = @Schema(implementation = ReservaHotel.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos")
    })
    @PostMapping
    public ResponseEntity<ReservaHotel> crear(@Valid @RequestBody ReservaHotelDTO dto) {
        log.info("POST /api/reservas-hotel - usuario {}", dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todas las reservas de hotel", description = "Retorna el listado completo de reservas de hotel registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ReservaHotel>> listarTodas() {
        log.info("GET /api/reservas-hotel");
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Buscar reserva de hotel por ID", description = "Retorna una reserva específica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservaHotel> buscarPorId(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        log.info("GET /api/reservas-hotel/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar reservas por usuario", description = "Retorna todas las reservas de hotel realizadas por un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas del usuario encontradas")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaHotel>> buscarPorUsuario(@Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        log.info("GET /api/reservas-hotel/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Cambiar estado de una reserva", description = "Actualiza el estado de una reserva de hotel (ej: CONFIRMADA, CANCELADA, PENDIENTE)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaHotel> cambiarEstado(
            @Parameter(description = "ID de la reserva") @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la reserva") @RequestParam ReservaHotel.EstadoReserva nuevoEstado) {
        log.info("PUT /api/reservas-hotel/{}/estado", id);
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }

    @Operation(summary = "Eliminar una reserva de hotel", description = "Elimina una reserva de hotel del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        log.info("DELETE /api/reservas-hotel/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener hotel asociado a la reserva", description = "Consulta el microservicio de hoteles para obtener el detalle del hotel vinculado a esta reserva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva o hotel no encontrados")
    })
    @GetMapping("/{id}/hotel")
    public ResponseEntity<Map<String, Object>> obtenerHotelDeLaReserva(@Parameter(description = "ID de la reserva") @PathVariable Long id) {
        log.info("GET /api/reservas-hotel/{}/hotel - consultando ms-hoteles", id);
        ReservaHotel reserva = service.buscarPorId(id);
        Map<String, Object> hotel = hotelClientService.buscarHotelPorId(reserva.getHotelId());
        return ResponseEntity.ok(hotel);
    }
}