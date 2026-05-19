package com.tuequipo.ms_resenas.service;

import com.tuequipo.ms_resenas.dto.ResenaDTO;
import com.tuequipo.ms_resenas.model.Resena;
import com.tuequipo.ms_resenas.repository.ResenaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio de negocio para resenas.
 * Valida que el usuario exista en ms-usuarios antes de registrar la resena.
 */
@Service
public class ResenaService {

    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);
    private final ResenaRepository repository;
    private final UsuarioClientService usuarioClientService;

    public ResenaService(ResenaRepository repository, UsuarioClientService usuarioClientService) {
        this.repository = repository;
        this.usuarioClientService = usuarioClientService;
    }

    /**
     * Crea una resena verificando que el usuario exista en ms-usuarios.
     */
    public Resena crear(ResenaDTO dto) {
        log.info("Creando resena de usuario {} para referencia {} tipo {}",
                dto.getUsuarioId(), dto.getReferenciaId(), dto.getTipo());

        // Regla de negocio: verificar usuario en ms-usuarios antes de aceptar la resena
        usuarioClientService.verificarUsuario(dto.getUsuarioId());

        // Regla de negocio: un usuario no puede reseñar el mismo elemento dos veces
        List<Resena> existentes = repository.findByReferenciaIdAndTipo(dto.getReferenciaId(), dto.getTipo());
        boolean yaResenado = existentes.stream()
                .anyMatch(r -> r.getUsuarioId().equals(dto.getUsuarioId()));
        if (yaResenado) {
            log.warn("Usuario {} ya tiene una resena para referencia {} tipo {}",
                    dto.getUsuarioId(), dto.getReferenciaId(), dto.getTipo());
            throw new RuntimeException("El usuario ya tiene una resena para este elemento");
        }

        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setReferenciaId(dto.getReferenciaId());
        resena.setTipo(dto.getTipo());
        resena.setCalificacion(dto.getCalificacion());
        resena.setComentario(dto.getComentario());
        Resena guardada = repository.save(resena);
        log.info("Resena creada con ID {}", guardada.getId());
        return guardada;
    }

    public List<Resena> listarTodas() {
        log.info("Listando todas las resenas");
        return repository.findAll();
    }

    public Resena buscarPorId(Long id) {
        log.info("Buscando resena con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada con ID: " + id));
    }

    public List<Resena> buscarPorUsuario(Long usuarioId) {
        log.info("Buscando resenas del usuario {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Resena> buscarPorReferencia(Long referenciaId, Resena.TipoResena tipo) {
        log.info("Buscando resenas de referencia {} tipo {}", referenciaId, tipo);
        return repository.findByReferenciaIdAndTipo(referenciaId, tipo);
    }

    public void eliminar(Long id) {
        log.info("Eliminando resena con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Resena {} eliminada", id);
    }
}
