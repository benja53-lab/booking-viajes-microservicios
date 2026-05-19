package com.tuequipo.ms_reservas_vuelo.repository;

import com.tuequipo.ms_reservas_vuelo.model.ReservaVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaVueloRepository extends JpaRepository<ReservaVuelo, Long> {
    List<ReservaVuelo> findByUsuarioId(Long usuarioId);
    List<ReservaVuelo> findByEstado(ReservaVuelo.EstadoReserva estado);
}