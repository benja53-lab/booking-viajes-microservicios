package com.tuequipo.ms_notificaciones.service;

import com.tuequipo.ms_notificaciones.dto.NotificacionDTO;
import com.tuequipo.ms_notificaciones.model.Notificacion;
import com.tuequipo.ms_notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository repository;

    public NotificacionService(NotificacionRepository repository) {
        this.repository = repository;
    }

    public Notificacion crear(NotificacionDTO dto) {
        log.info("Creando notificacion para usuario {}", dto.getUsuarioId());
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());
        Notificacion guardada = repository.save(notificacion);
        log.info("Notificacion creada con ID {}", guardada.getId());
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