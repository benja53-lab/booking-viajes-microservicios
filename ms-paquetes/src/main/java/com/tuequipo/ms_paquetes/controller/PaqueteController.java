package com.tuequipo.ms_paquetes.controller;

import com.tuequipo.ms_paquetes.dto.PaqueteDTO;
import com.tuequipo.ms_paquetes.model.Paquete;
import com.tuequipo.ms_paquetes.service.HotelClientService;
import com.tuequipo.ms_paquetes.service.PaqueteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public ResponseEntity<Paquete> crear(@Valid @RequestBody PaqueteDTO dto) {
        log.info("POST /api/paquetes");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Paquete>> listarTodos() {
        log.info("GET /api/paquetes");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paquete> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/paquetes/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Paquete>> buscarDisponibles() {
        log.info("GET /api/paquetes/disponibles");
        return ResponseEntity.ok(service.buscarDisponibles());
    }

    @GetMapping("/destino/{destinoId}")
    public ResponseEntity<List<Paquete>> buscarPorDestino(@PathVariable Long destinoId) {
        log.info("GET /api/paquetes/destino/{}", destinoId);
        return ResponseEntity.ok(service.buscarPorDestino(destinoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paquete> actualizar(@PathVariable Long id, @Valid @RequestBody PaqueteDTO dto) {
        log.info("PUT /api/paquetes/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/paquetes/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Comunicacion entre microservicios — consulta hotel desde ms-hoteles
    @GetMapping("/{id}/hotel")
    public ResponseEntity<Map> obtenerHotelDePaquete(@PathVariable Long id) {
        log.info("GET /api/paquetes/{}/hotel", id);
        Paquete paquete = service.buscarPorId(id);
        Map hotel = hotelClientService.buscarHotelPorId(paquete.getHotelId());
        return ResponseEntity.ok(hotel);
    }
}