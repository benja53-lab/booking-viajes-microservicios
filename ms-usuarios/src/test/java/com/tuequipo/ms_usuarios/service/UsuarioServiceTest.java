package com.tuequipo.ms_usuarios.service;

import com.tuequipo.ms_usuarios.dto.UsuarioDTO;
import com.tuequipo.ms_usuarios.model.Usuario;
import com.tuequipo.ms_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private UsuarioDTO dto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        dto = new UsuarioDTO();
        dto.setNombre("Benjamin");
        dto.setApellido("Cisternas");
        dto.setEmail("benjamin@mail.com");
        dto.setTelefono("912345678");
        dto.setNacionalidad("Chilena");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Benjamin");
        usuario.setApellido("Cisternas");
        usuario.setEmail("benjamin@mail.com");
        usuario.setTelefono("912345678");
        usuario.setNacionalidad("Chilena");
    }

    @Test
    void dadoUnDTO_cuandoCrear_entoncesRetornaUsuarioGuardado() {
        // GIVEN
        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // WHEN
        Usuario resultado = service.crear(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals("Benjamin", resultado.getNombre());
        assertEquals("benjamin@mail.com", resultado.getEmail());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void dadoEmailDuplicado_cuandoCrear_entoncesLanzaExcepcion() {
        // GIVEN
        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.crear(dto));
        assertTrue(ex.getMessage().contains("Ya existe un usuario con el email"));
        verify(repository, never()).save(any(Usuario.class));
    }

    @Test
    void dadoIdExistente_cuandoBuscarPorId_entoncesRetornaUsuario() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // WHEN
        Usuario resultado = service.buscarPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Benjamin", resultado.getNombre());
    }

    @Test
    void dadoIdInexistente_cuandoBuscarPorId_entoncesLanzaExcepcion() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
        assertTrue(ex.getMessage().contains("Usuario no encontrado con ID"));
    }

    @Test
    void cuandoListarTodos_entoncesRetornaLista() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(usuario));

        // WHEN
        List<Usuario> resultado = service.listarTodos();

        // THEN
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void dadoIdExistente_cuandoEliminar_entoncesEliminaCorrectamente() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(repository).deleteById(1L);

        // WHEN
        service.eliminar(1L);

        // THEN
        verify(repository, times(1)).deleteById(1L);
    }
}