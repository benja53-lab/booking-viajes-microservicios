package com.tuequipo.ms_resenas.controller;

import com.tuequipo.ms_resenas.dto.ResenaDTO;
import com.tuequipo.ms_resenas.model.Resena;
import com.tuequipo.ms_resenas.service.ResenaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    private static final Logger log = LoggerFactory.getLogger(ResenaController.class);
    private final ResenaService service;

    public ResenaController(ResenaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@Valid @RequestBody ResenaDTO dto) {
        log.info("POST /api/resenas");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Resena>> listarTodas() {
        log.info("GET /api/resenas");
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/resenas/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> buscarPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/resenas/usuario/{}", usuarioId);
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/referencia/{referenciaId}")
    public ResponseEntity<List<Resena>> buscarPorReferencia(
            @PathVariable Long referenciaId,
            @RequestParam Resena.TipoResena tipo) {
        log.info("GET /api/resenas/referencia/{}", referenciaId);
        return ResponseEntity.ok(service.buscarPorReferencia(referenciaId, tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/resenas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}