package com.tuequipo.ms_resenas.repository;

import com.tuequipo.ms_resenas.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByUsuarioId(Long usuarioId);
    List<Resena> findByReferenciaIdAndTipo(Long referenciaId, Resena.TipoResena tipo);
    List<Resena> findByTipo(Resena.TipoResena tipo);
}