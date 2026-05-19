package com.tuequipo.ms_destinos.repository;

import com.tuequipo.ms_destinos.model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Long> {
    List<Destino> findByPais(String pais);
    List<Destino> findByActivo(Boolean activo);
    List<Destino> findByCiudadContainingIgnoreCase(String ciudad);
}