package com.tuequipo.ms_destinos.controller;

import com.tuequipo.ms_destinos.dto.DestinoDTO;
import com.tuequipo.ms_destinos.model.Destino;
import com.tuequipo.ms_destinos.service.DestinoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

    private static final Logger log = LoggerFactory.getLogger(DestinoController.class);
    private final DestinoService service;

    public DestinoController(DestinoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Destino> crear(@Valid @RequestBody DestinoDTO dto) {
        log.info("POST /api/destinos");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Destino>> listarTodos() {
        log.info("GET /api/destinos");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destino> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/destinos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/pais/{pais}")
    public ResponseEntity<List<Destino>> buscarPorPais(@PathVariable String pais) {
        log.info("GET /api/destinos/pais/{}", pais);
        return ResponseEntity.ok(service.buscarPorPais(pais));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Destino>> buscarActivos() {
        log.info("GET /api/destinos/activos");
        return ResponseEntity.ok(service.buscarActivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Destino>> buscarPorCiudad(@RequestParam String ciudad) {
        log.info("GET /api/destinos/buscar?ciudad={}", ciudad);
        return ResponseEntity.ok(service.buscarPorCiudad(ciudad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destino> actualizar(@PathVariable Long id, @Valid @RequestBody DestinoDTO dto) {
        log.info("PUT /api/destinos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/destinos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}