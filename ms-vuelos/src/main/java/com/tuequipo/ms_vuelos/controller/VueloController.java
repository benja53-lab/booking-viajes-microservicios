package com.tuequipo.ms_vuelos.controller;

import com.tuequipo.ms_vuelos.dto.VueloDTO;
import com.tuequipo.ms_vuelos.model.Vuelo;
import com.tuequipo.ms_vuelos.service.VueloService;
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

@Tag(name = "Vuelos", description = "Gestión de vuelos disponibles en el sistema")
@RestController
@RequestMapping("/api/vuelos")
public class VueloController {

    private static final Logger log = LoggerFactory.getLogger(VueloController.class);
    private final VueloService service;

    public VueloController(VueloService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un vuelo", description = "Registra un nuevo vuelo en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Vuelo creado exitosamente",
            content = @Content(schema = @Schema(implementation = Vuelo.class))),
        @ApiResponse(responseCode = "400", description = "Datos del vuelo inválidos")
    })
    @PostMapping
    public ResponseEntity<Vuelo> crear(@Valid @RequestBody VueloDTO dto) {
        log.info("POST /api/vuelos");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar todos los vuelos", description = "Retorna el listado completo de vuelos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Vuelo>> listarTodos() {
        log.info("GET /api/vuelos");
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar vuelo por ID", description = "Retorna un vuelo específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vuelo encontrado"),
        @ApiResponse(responseCode = "404", description = "Vuelo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Vuelo> buscarPorId(@Parameter(description = "ID del vuelo") @PathVariable Long id) {
        log.info("GET /api/vuelos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Buscar vuelos activos", description = "Retorna el listado de vuelos actualmente activos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vuelos activos encontrados")
    })
    @GetMapping("/activos")
    public ResponseEntity<List<Vuelo>> buscarActivos() {
        log.info("GET /api/vuelos/activos");
        return ResponseEntity.ok(service.buscarActivos());
    }

    @Operation(summary = "Buscar vuelos por ruta", description = "Retorna los vuelos disponibles según origen y destino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vuelos encontrados para la ruta")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<Vuelo>> buscarPorRuta(
            @Parameter(description = "Ciudad de origen") @RequestParam String origen,
            @Parameter(description = "Ciudad de destino") @RequestParam String destino) {
        log.info("GET /api/vuelos/buscar?origen={}&destino={}", origen, destino);
        return ResponseEntity.ok(service.buscarPorRuta(origen, destino));
    }

    @Operation(summary = "Actualizar un vuelo", description = "Actualiza los datos de un vuelo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vuelo actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Vuelo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Vuelo> actualizar(@Parameter(description = "ID del vuelo") @PathVariable Long id, @Valid @RequestBody VueloDTO dto) {
        log.info("PUT /api/vuelos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar un vuelo", description = "Elimina un vuelo del sistema según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Vuelo eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Vuelo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del vuelo") @PathVariable Long id) {
        log.info("DELETE /api/vuelos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}