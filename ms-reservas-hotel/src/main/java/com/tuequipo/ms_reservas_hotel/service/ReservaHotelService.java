package com.tuequipo.ms_reservas_hotel.service;

import com.tuequipo.ms_reservas_hotel.dto.ReservaHotelDTO;
import com.tuequipo.ms_reservas_hotel.model.ReservaHotel;
import com.tuequipo.ms_reservas_hotel.repository.ReservaHotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaHotelService {

    private static final Logger log = LoggerFactory.getLogger(ReservaHotelService.class);
    private final ReservaHotelRepository repository;

    public ReservaHotelService(ReservaHotelRepository repository) {
        this.repository = repository;
    }

    public ReservaHotel crear(ReservaHotelDTO dto) {
        log.info("Creando reserva de hotel para usuario {}", dto.getUsuarioId());

        if (dto.getFechaCheckOut().isBefore(dto.getFechaCheckIn()) ||
            dto.getFechaCheckOut().isEqual(dto.getFechaCheckIn())) {
            throw new RuntimeException("La fecha de check-out debe ser posterior al check-in");
        }

        ReservaHotel reserva = new ReservaHotel();
        reserva.setUsuarioId(dto.getUsuarioId());
        reserva.setHotelId(dto.getHotelId());
        reserva.setFechaCheckIn(dto.getFechaCheckIn());
        reserva.setFechaCheckOut(dto.getFechaCheckOut());
        reserva.setCantidadHuespedes(dto.getCantidadHuespedes());
        reserva.setPrecioTotal(dto.getPrecioTotal());

        ReservaHotel guardada = repository.save(reserva);
        log.info("Reserva de hotel creada con ID {}", guardada.getId());
        return guardada;
    }

    public List<ReservaHotel> listarTodas() {
        log.info("Listando todas las reservas de hotel");
        return repository.findAll();
    }

    public ReservaHotel buscarPorId(Long id) {
        log.info("Buscando reserva de hotel con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva de hotel no encontrada con ID: " + id));
    }

    public List<ReservaHotel> buscarPorUsuario(Long usuarioId) {
        log.info("Buscando reservas del usuario {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public ReservaHotel cambiarEstado(Long id, ReservaHotel.EstadoReserva nuevoEstado) {
        log.info("Cambiando estado de reserva {} a {}", id, nuevoEstado);
        ReservaHotel reserva = buscarPorId(id);
        reserva.setEstado(nuevoEstado);
        ReservaHotel actualizada = repository.save(reserva);
        log.info("Estado actualizado para reserva {}", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Eliminando reserva de hotel con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Reserva de hotel {} eliminada", id);
    }
}