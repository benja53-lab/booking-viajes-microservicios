package com.tuequipo.ms_usuarios.repository;

import com.tuequipo.ms_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByActivo(Boolean activo);
    List<Usuario> findByNacionalidad(String nacionalidad);
}