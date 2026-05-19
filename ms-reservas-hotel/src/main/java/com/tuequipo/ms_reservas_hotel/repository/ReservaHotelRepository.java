package com.tuequipo.ms_reservas_hotel.repository;

import com.tuequipo.ms_reservas_hotel.model.ReservaHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaHotelRepository extends JpaRepository<ReservaHotel, Long> {
    List<ReservaHotel> findByUsuarioId(Long usuarioId);
    List<ReservaHotel> findByHotelId(Long hotelId);
    List<ReservaHotel> findByEstado(ReservaHotel.EstadoReserva estado);
}