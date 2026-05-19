package com.tuequipo.ms_notificaciones.service;

import com.tuequipo.ms_notificaciones.dto.NotificacionDTO;
import com.tuequipo.ms_notificaciones.model.Notificacion;
import com.tuequipo.ms_notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio de negocio para notificaciones.
 * Verifica que el usuario destinatario exista en ms-usuarios antes de crear la notificacion.
 */
@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository repository;
    private final UsuarioClientService usuarioClientService;

    public NotificacionService(NotificacionRepository repository,
                               UsuarioClientService usuarioClientService) {
        this.repository = repository;
        this.usuarioClientService = usuarioClientService;
    }

    /**
     * Crea una notificacion verificando previamente que el usuario existe en ms-usuarios.
     */
    public Notificacion crear(NotificacionDTO dto) {
        log.info("Creando notificacion tipo {} para usuario {}", dto.getTipo(), dto.getUsuarioId());

        // Regla de negocio: verificar que el usuario existe antes de notificar
        usuarioClientService.buscarUsuarioPorId(dto.getUsuarioId());

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());
        Notificacion guardada = repository.save(notificacion);
        log.info("Notificacion creada con ID {} para usuario {}", guardada.getId(), dto.getUsuarioId());
        return guardada;
    }

    public List<Notificacion> listarTodas() {
        log.info("Listando todas las notificaciones");
        return repository.findAll();
    }

    public Notificacion buscarPorId(Long id) {
        log.info("Buscando notificacion con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada con ID: " + id));
    }

    public List<Notificacion> buscarPorUsuario(Long usuarioId) {
        log.info("Buscando notificaciones del usuario {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Notificacion> buscarNoLeidas(Long usuarioId) {
        log.info("Buscando notificaciones no leidas del usuario {}", usuarioId);
        return repository.findByUsuarioIdAndLeida(usuarioId, false);
    }

    public Notificacion marcarComoLeida(Long id) {
        log.info("Marcando notificacion {} como leida", id);
        Notificacion notificacion = buscarPorId(id);
        notificacion.setLeida(true);
        Notificacion actualizada = repository.save(notificacion);
        log.info("Notificacion {} marcada como leida", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Eliminando notificacion con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Notificacion {} eliminada", id);
    }
}
