package com.tuequipo.ms_hoteles.controller;

import com.tuequipo.ms_hoteles.dto.HotelDTO;
import com.tuequipo.ms_hoteles.model.Hotel;
import com.tuequipo.ms_hoteles.service.HotelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hoteles")
public class HotelController {

    private static final Logger log = LoggerFactory.getLogger(HotelController.class);
    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Hotel> crear(@Valid @RequestBody HotelDTO dto) {
        log.info("POST /api/hoteles");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> listarTodos() {
        log.info("GET /api/hoteles");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/hoteles/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Hotel>> buscarPorCiudad(@PathVariable String ciudad) {
        log.info("GET /api/hoteles/ciudad/{}", ciudad);
        return ResponseEntity.ok(service.buscarPorCiudad(ciudad));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Hotel>> buscarDisponibles() {
        log.info("GET /api/hoteles/disponibles");
        return ResponseEntity.ok(service.buscarDisponibles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> actualizar(@PathVariable Long id, @Valid @RequestBody HotelDTO dto) {
        log.info("PUT /api/hoteles/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/hoteles/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}