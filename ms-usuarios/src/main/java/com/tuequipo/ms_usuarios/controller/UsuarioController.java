package com.tuequipo.ms_usuarios.controller;

import com.tuequipo.ms_usuarios.dto.UsuarioDTO;
import com.tuequipo.ms_usuarios.model.Usuario;
import com.tuequipo.ms_usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /api/usuarios");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        log.info("GET /api/usuarios");
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/usuarios/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        log.info("GET /api/usuarios/email/{}", email);
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        log.info("PUT /api/usuarios/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/usuarios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}