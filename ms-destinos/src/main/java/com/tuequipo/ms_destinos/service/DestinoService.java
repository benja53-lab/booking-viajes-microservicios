package com.tuequipo.ms_destinos.service;

import com.tuequipo.ms_destinos.dto.DestinoDTO;
import com.tuequipo.ms_destinos.model.Destino;
import com.tuequipo.ms_destinos.repository.DestinoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DestinoService {

    private static final Logger log = LoggerFactory.getLogger(DestinoService.class);
    private final DestinoRepository repository;

    public DestinoService(DestinoRepository repository) {
        this.repository = repository;
    }

    public Destino crear(DestinoDTO dto) {
        log.info("Creando destino: {} - {}", dto.getCiudad(), dto.getPais());
        Destino destino = new Destino();
        destino.setCiudad(dto.getCiudad());
        destino.setPais(dto.getPais());
        destino.setDescripcion(dto.getDescripcion());
        destino.setIdioma(dto.getIdioma());
        destino.setMoneda(dto.getMoneda());
        destino.setActivo(dto.getActivo());
        Destino guardado = repository.save(destino);
        log.info("Destino creado con ID {}", guardado.getId());
        return guardado;
    }

    public List<Destino> listarTodos() {
        log.info("Listando todos los destinos");
        return repository.findAll();
    }

    public Destino buscarPorId(Long id) {
        log.info("Buscando destino con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destino no encontrado con ID: " + id));
    }

    public List<Destino> buscarPorPais(String pais) {
        log.info("Buscando destinos en pais: {}", pais);
        return repository.findByPais(pais);
    }

    public List<Destino> buscarActivos() {
        log.info("Buscando destinos activos");
        return repository.findByActivo(true);
    }

    public List<Destino> buscarPorCiudad(String ciudad) {
        log.info("Buscando destinos por ciudad: {}", ciudad);
        return repository.findByCiudadContainingIgnoreCase(ciudad);
    }

    public Destino actualizar(Long id, DestinoDTO dto) {
        log.info("Actualizando destino con ID {}", id);
        Destino destino = buscarPorId(id);
        destino.setCiudad(dto.getCiudad());
        destino.setPais(dto.getPais());
        destino.setDescripcion(dto.getDescripcion());
        destino.setIdioma(dto.getIdioma());
        destino.setMoneda(dto.getMoneda());
        destino.setActivo(dto.getActivo());
        Destino actualizado = repository.save(destino);
        log.info("Destino {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando destino con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Destino {} eliminado", id);
    }
}