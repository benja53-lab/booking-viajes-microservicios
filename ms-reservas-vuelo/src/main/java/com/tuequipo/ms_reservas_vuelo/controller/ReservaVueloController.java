package com.tuequipo.ms_reservas_vuelo.controller;

import com.tuequipo.ms_reservas_vuelo.dto.ReservaVueloDTO;
import com.tuequipo.ms_reservas_vuelo.model.ReservaVuelo;
import com.tuequipo.ms_reservas_vuelo.service.ReservaVueloService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservas-vuelo")
public class ReservaVueloController {

    private static final Logger log = LoggerFactory.getLogger(ReservaVueloController.class);

    private final ReservaVueloService service;

    public ReservaVueloController(ReservaVueloService service) {
        this.service = service;
    }

    // ── POST /api/reservas-vuelo ───────────────────────────
    @PostMapping
    public ResponseEntity<ReservaVuelo> crear(@Valid @RequestBody ReservaVueloDTO dto) {
        log.info("POST /api/reservas-vuelo - creando reserva");
        ReservaVuelo creada = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada); // 201
    }

    // ── GET /api/reservas-vuelo ────────────────────────────
    @GetMapping
    public ResponseEntity<List<ReservaVuelo>> listarTodas() {
        log.info("GET /api/reservas-vuelo - listando todas");
        List<ReservaVuelo> reservas = service.listarTodas();
        return ResponseEntity.ok(reservas); // 200
    }

    // ── GET /api/reservas-vuelo/{id} ───────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ReservaVuelo> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/reservas-vuelo/{}", id);
        ReservaVuelo reserva = service.buscarPorId(id);
        return ResponseEntity.ok(reserva); // 200
    }

    // ── GET /api/reservas-vuelo/usuario/{usuarioId} ────────
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaVuelo>> buscarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/reservas-vuelo/usuario/{}", usuarioId);
        List<ReservaVuelo> reservas = service.buscarPorUsuario(usuarioId);
        return ResponseEntity.ok(reservas); // 200
    }

    // ── PUT /api/reservas-vuelo/{id}/estado ────────────────
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaVuelo> cambiarEstado(
            @PathVariable Long id,
            @RequestParam ReservaVuelo.EstadoReserva nuevoEstado) {
        log.info("PUT /api/reservas-vuelo/{}/estado - nuevo estado: {}", id, nuevoEstado);
        ReservaVuelo actualizada = service.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada); // 200
    }

    // ── DELETE /api/reservas-vuelo/{id} ───────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/reservas-vuelo/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }
}