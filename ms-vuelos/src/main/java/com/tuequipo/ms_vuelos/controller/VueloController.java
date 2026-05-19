package com.tuequipo.ms_vuelos.controller;

import com.tuequipo.ms_vuelos.dto.VueloDTO;
import com.tuequipo.ms_vuelos.model.Vuelo;
import com.tuequipo.ms_vuelos.service.VueloService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
public class VueloController {

    private static final Logger log = LoggerFactory.getLogger(VueloController.class);
    private final VueloService service;

    public VueloController(VueloService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Vuelo> crear(@Valid @RequestBody VueloDTO dto) {
        log.info("POST /api/vuelos");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Vuelo>> listarTodos() {
        log.info("GET /api/vuelos");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vuelo> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/vuelos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Vuelo>> buscarActivos() {
        log.info("GET /api/vuelos/activos");
        return ResponseEntity.ok(service.buscarActivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Vuelo>> buscarPorRuta(
            @RequestParam String origen,
            @RequestParam String destino) {
        log.info("GET /api/vuelos/buscar?origen={}&destino={}", origen, destino);
        return ResponseEntity.ok(service.buscarPorRuta(origen, destino));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vuelo> actualizar(@PathVariable Long id, @Valid @RequestBody VueloDTO dto) {
        log.info("PUT /api/vuelos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/vuelos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}