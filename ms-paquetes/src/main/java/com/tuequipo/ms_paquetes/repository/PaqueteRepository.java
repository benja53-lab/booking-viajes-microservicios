package com.tuequipo.ms_paquetes.repository;

import com.tuequipo.ms_paquetes.model.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    List<Paquete> findByDisponible(Boolean disponible);
    List<Paquete> findByDestinoId(Long destinoId);
    List<Paquete> findByNombreContainingIgnoreCase(String nombre);
}