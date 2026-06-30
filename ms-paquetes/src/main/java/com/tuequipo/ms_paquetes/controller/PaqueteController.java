package com.tuequipo.ms_paquetes.controller;

import com.tuequipo.ms_paquetes.dto.PaqueteDTO;
import com.tuequipo.ms_paquetes.model.Paquete;
import com.tuequipo.ms_paquetes.service.HotelClientService;
import com.tuequipo.ms_paquetes.service.PaqueteService;
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

@Tag(name = "Paquetes", description = "Gestión de paquetes turísticos que combinan hoteles y destinos")
@RestController
@RequestMapping("/api/paquetes")
public class PaqueteController {

    private static final Logger log = LoggerFactory.getLogger(PaqueteController.class);
    private final PaqueteService service;
    private final HotelClientService hotelClientService;

    public PaqueteController(PaqueteService service, HotelClientService hotelClientService) {
        this.service = service;
        this.hotelClientService = hotelClientService;
    }

    @Operation(summary = "Crear un paquete turístico", description = "Registra un nuevo paquete turístico en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paquete creado exitosamente",
            content = @Content(schema = @Schema(implementation = Paquete.class))),
        @ApiResponse(responseCode = "400", description = "Datos del paquete inválidos")
    })
    @PostMapping
    public ResponseEntity<Paquete> crear(@Valid @RequestBody PaqueteDTO dto) {
        log.info("POST /api/paquetes");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todos los paquetes", description = "Retorna el listado completo de paquetes turísticos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Paquete>> listarTodos() {
        log.info("GET /api/paquetes");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar paquete por ID", description = "Retorna un paquete turístico específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paquete encontrado"),
        @ApiResponse(responseCode = "404", description = "Paquete no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Paquete> buscarPorId(@Parameter(description = "ID del paquete") @PathVariable Long id) {
        log.info("GET /api/paquetes/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar paquetes disponibles", description = "Retorna el listado de paquetes turísticos actualmente disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paquetes disponibles encontrados")
    })
    @GetMapping("/disponibles")
    public ResponseEntity<List<Paquete>> buscarDisponibles() {
        log.info("GET /api/paquetes/disponibles");
        return ResponseEntity.ok(service.buscarDisponibles());
    }

    @Operation(summary = "Buscar paquetes por destino", description = "Retorna los paquetes turísticos asociados a un destino específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paquetes del destino encontrados")
    })
    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<Paquete>> buscarPorDestino(@Parameter(description = "ID del destino") @PathVariable Long destinoId) {
        log.info("GET /api/paquetes/destino/{}", destinoId);
        return ResponseEntity.ok(service.buscarPorDestino(destinoId));
    }

    @Operation(summary = "Actualizar un paquete", description = "Actualiza los datos de un paquete turístico existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paquete actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Paquete no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Paquete> actualizar(@Parameter(description = "ID del paquete") @PathVariable Long id, @Valid @RequestBody PaqueteDTO dto) {
        log.info("PUT /api/paquetes/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un paquete", description = "Elimina un paquete turístico del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Paquete eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Paquete no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del paquete") @PathVariable Long id) {
        log.info("DELETE /api/paquetes/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener hotel asociado al paquete", description = "Consulta el microservicio de hoteles para obtener el detalle del hotel vinculado a este paquete")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hotel obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Paquete o hotel no encontrados")
    })
    @GetMapping("/{id}/hotel")
    public ResponseEntity<Map> obtenerHotelDePaquete(@Parameter(description = "ID del paquete") @PathVariable Long id) {
        log.info("GET /api/paquetes/{}/hotel", id);
        Paquete paquete = service.buscarPorId(id);
        Map hotel = hotelClientService.buscarHotelPorId(paquete.getHotelId());
        return ResponseEntity.ok(hotel);
    }
}