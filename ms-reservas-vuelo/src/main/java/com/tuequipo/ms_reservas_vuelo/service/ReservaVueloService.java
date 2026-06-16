package com.tuequipo.ms_reservas_vuelo.service;

import com.tuequipo.ms_reservas_vuelo.dto.ReservaVueloDTO;
import com.tuequipo.ms_reservas_vuelo.model.ReservaVuelo;
import com.tuequipo.ms_reservas_vuelo.repository.ReservaVueloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Servicio de negocio para reservas de vuelo.
 * Valida disponibilidad consultando ms-vuelos antes de confirmar la reserva.
 */
@Service
public class ReservaVueloService {

    private static final Logger log = LoggerFactory.getLogger(ReservaVueloService.class);

    private final ReservaVueloRepository repository;
    private final VueloClientService vueloClientService;

    public ReservaVueloService(ReservaVueloRepository repository,
                               VueloClientService vueloClientService) {
        this.repository = repository;
        this.vueloClientService = vueloClientService;
    }

    /**
     * Crea una reserva de vuelo validando que el vuelo exista y tenga asientos disponibles.
     */
    public ReservaVuelo crear(ReservaVueloDTO dto) {
        log.info("Iniciando creacion de reserva para usuario {} en vuelo {}",
                dto.getUsuarioId(), dto.getVueloId());

        // Regla de negocio: verificar que el vuelo existe y tiene asientos disponibles
        Map<String, Object> vuelo = vueloClientService.buscarVueloPorId(dto.getVueloId());

        Boolean activo = (Boolean) vuelo.get("activo");
        if (activo == null || !activo) {
            log.warn("Intento de reserva en vuelo inactivo: {}", dto.getVueloId());
            throw new RuntimeException("El vuelo con ID " + dto.getVueloId() + " no esta activo");
        }

        Integer asientosDisponibles = (Integer) vuelo.get("asientosDisponibles");
        if (asientosDisponibles == null || asientosDisponibles < dto.getCantidadAsientos()) {
            log.warn("Asientos insuficientes en vuelo {}: disponibles={}, solicitados={}",
                    dto.getVueloId(), asientosDisponibles, dto.getCantidadAsientos());
            throw new RuntimeException("Asientos insuficientes. Disponibles: " + asientosDisponibles);
        }

        ReservaVuelo reserva = new ReservaVuelo();
        reserva.setUsuarioId(dto.getUsuarioId());
        reserva.setVueloId(dto.getVueloId());
        reserva.setCantidadAsientos(dto.getCantidadAsientos());
        reserva.setPrecioTotal(dto.getPrecioTotal());

        ReservaVuelo guardada = repository.save(reserva);
        log.info("Reserva de vuelo creada con ID {}", guardada.getId());
        return guardada;
    }

    public List<ReservaVuelo> listarTodas() {
        log.info("Listando todas las reservas de vuelo");
        return repository.findAll();
    }

    public ReservaVuelo buscarPorId(Long id) {
        log.info("Buscando reserva de vuelo con ID {}", id);
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
        log.info("Eliminando reserva de vuelo con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Reserva de vuelo {} eliminada", id);
    }
}
