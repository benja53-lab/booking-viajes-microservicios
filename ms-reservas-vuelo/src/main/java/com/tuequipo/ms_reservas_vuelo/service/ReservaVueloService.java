package com.tuequipo.ms_reservas_vuelo.service;

import com.tuequipo.ms_reservas_vuelo.dto.ReservaVueloDTO;
import com.tuequipo.ms_reservas_vuelo.model.ReservaVuelo;
import com.tuequipo.ms_reservas_vuelo.repository.ReservaVueloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaVueloService {

    private static final Logger log = LoggerFactory.getLogger(ReservaVueloService.class);

    private final ReservaVueloRepository repository;

    public ReservaVueloService(ReservaVueloRepository repository) {
        this.repository = repository;
    }

    public ReservaVuelo crear(ReservaVueloDTO dto) {
        log.info("Creando reserva para usuario {} en vuelo {}", dto.getUsuarioId(), dto.getVueloId());
        ReservaVuelo reserva = new ReservaVuelo();
        reserva.setUsuarioId(dto.getUsuarioId());
        reserva.setVueloId(dto.getVueloId());
        reserva.setCantidadAsientos(dto.getCantidadAsientos());
        reserva.setPrecioTotal(dto.getPrecioTotal());
        ReservaVuelo guardada = repository.save(reserva);
        log.info("Reserva creada con ID {}", guardada.getId());
        return guardada;
    }

    public List<ReservaVuelo> listarTodas() {
        log.info("Listando todas las reservas");
        return repository.findAll();
    }

    public ReservaVuelo buscarPorId(Long id) {
        log.info("Buscando reserva con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    public List<ReservaVuelo> buscarPorUsuario(Long usuarioId) {
        log.info("Buscando reservas del usuario {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public ReservaVuelo cambiarEstado(Long id, ReservaVuelo.EstadoReserva nuevoEstado) {
        log.info("Cambiando estado de reserva {} a {}", id, nuevoEstado);
        ReservaVuelo reserva = buscarPorId(id);
        reserva.setEstado(nuevoEstado);
        ReservaVuelo actualizada = repository.save(reserva);
        log.info("Estado actualizado correctamente para reserva {}", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Eliminando reserva con ID {}", id);
        ReservaVuelo reserva = buscarPorId(id);
        repository.deleteById(id);
        log.warn("Reserva {} eliminada", id);
    }
}