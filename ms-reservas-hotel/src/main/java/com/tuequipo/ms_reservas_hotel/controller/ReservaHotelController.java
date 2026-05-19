package com.tuequipo.ms_reservas_hotel.controller;

import com.tuequipo.ms_reservas_hotel.dto.ReservaHotelDTO;
import com.tuequipo.ms_reservas_hotel.model.ReservaHotel;
import com.tuequipo.ms_reservas_hotel.service.ReservaHotelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas-hotel")
public class ReservaHotelController {

    private static final Logger log = LoggerFactory.getLogger(ReservaHotelController.class);
    private final ReservaHotelService service;

    public ReservaHotelController(ReservaHotelService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservaHotel> crear(@Valid @RequestBody ReservaHotelDTO dto) {
        log.info("POST /api/reservas-hotel");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservaHotel>> listarTodas() {
        log.info("GET /api/reservas-hotel");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaHotel> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/reservas-hotel/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaHotel>> buscarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/reservas-hotel/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaHotel> cambiarEstado(
            @PathVariable Long id,
            @RequestParam ReservaHotel.EstadoReserva nuevoEstado) {
        log.info("PUT /api/reservas-hotel/{}/estado", id);
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/reservas-hotel/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}