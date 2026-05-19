package com.tuequipo.ms_usuarios.service;

import com.tuequipo.ms_usuarios.dto.UsuarioDTO;
import com.tuequipo.ms_usuarios.model.Usuario;
import com.tuequipo.ms_usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crear(UsuarioDTO dto) {
        log.info("Creando usuario: {} {}", dto.getNombre(), dto.getApellido());
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setNacionalidad(dto.getNacionalidad());
        Usuario guardado = repository.save(usuario);
        log.info("Usuario creado con ID {}", guardado.getId());
        return guardado;
    }

    public List<Usuario> listarTodos() {
        log.info("Listando todos los usuarios");
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        log.info("Buscando usuario con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario buscarPorEmail(String email) {
        log.info("Buscando usuario con email {}", email);
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public Usuario actualizar(Long id, UsuarioDTO dto) {
        log.info("Actualizando usuario con ID {}", id);
        Usuario usuario = buscarPorId(id);
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setNacionalidad(dto.getNacionalidad());
        Usuario actualizado = repository.save(usuario);
        log.info("Usuario {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Usuario {} eliminado", id);
    }
}