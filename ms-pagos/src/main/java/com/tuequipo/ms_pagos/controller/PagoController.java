package com.tuequipo.ms_pagos.controller;

import com.tuequipo.ms_pagos.dto.PagoDTO;
import com.tuequipo.ms_pagos.model.Pago;
import com.tuequipo.ms_pagos.service.PagoService;
import com.tuequipo.ms_pagos.service.ReservaClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        log.info("POST /api/pagos - reserva {} tipo {}", dto.getReservaId(), dto.getTipoReserva());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        log.info("GET /api/pagos");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/pagos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> buscarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/pagos/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Pago.EstadoPago nuevoEstado) {
        log.info("PUT /api/pagos/{}/estado -> {}", id, nuevoEstado);
        return ResponseEntity.ok(service.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/pagos/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Comunicacion entre microservicios — consulta la reserva asociada al pago
    @GetMapping("/{id}/reserva")
    public ResponseEntity<Map<String, Object>> obtenerReservaDePago(@PathVariable Long id) {
        log.info("GET /api/pagos/{}/reserva - consultando microservicio de reservas", id);
        Pago pago = service.buscarPorId(id);
        Map<String, Object> reserva = reservaClientService.verificarReserva(
                pago.getReservaId(), pago.getTipoReserva());
        return ResponseEntity.ok(reserva);
    }
}
