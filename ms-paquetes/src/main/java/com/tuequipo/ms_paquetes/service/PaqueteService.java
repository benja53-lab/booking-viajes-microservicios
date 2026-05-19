package com.tuequipo.ms_paquetes.service;

import com.tuequipo.ms_paquetes.dto.PaqueteDTO;
import com.tuequipo.ms_paquetes.model.Paquete;
import com.tuequipo.ms_paquetes.repository.PaqueteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaqueteService {

    private static final Logger log = LoggerFactory.getLogger(PaqueteService.class);
    private final PaqueteRepository repository;

    public PaqueteService(PaqueteRepository repository) {
        this.repository = repository;
    }

    public Paquete crear(PaqueteDTO dto) {
        log.info("Creando paquete: {}", dto.getNombre());
        Paquete paquete = new Paquete();
        paquete.setNombre(dto.getNombre());
        paquete.setDescripcion(dto.getDescripcion());
        paquete.setVueloId(dto.getVueloId());
        paquete.setHotelId(dto.getHotelId());
        paquete.setDestinoId(dto.getDestinoId());
        paquete.setDuracionDias(dto.getDuracionDias());
        paquete.setPrecioTotal(dto.getPrecioTotal());
        paquete.setDisponible(dto.getDisponible());
        Paquete guardado = repository.save(paquete);
        log.info("Paquete creado con ID {}", guardado.getId());
        return guardado;
    }

    public List<Paquete> listarTodos() {
        log.info("Listando todos los paquetes");
        return repository.findAll();
    }

    public Paquete buscarPorId(Long id) {
        log.info("Buscando paquete con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado con ID: " + id));
    }

    public List<Paquete> buscarDisponibles() {
        log.info("Buscando paquetes disponibles");
        return repository.findByDisponible(true);
    }

    public List<Paquete> buscarPorDestino(Long destinoId) {
        log.info("Buscando paquetes para destino {}", destinoId);
        return repository.findByDestinoId(destinoId);
    }

    public Paquete actualizar(Long id, PaqueteDTO dto) {
        log.info("Actualizando paquete con ID {}", id);
        Paquete paquete = buscarPorId(id);
        paquete.setNombre(dto.getNombre());
        paquete.setDescripcion(dto.getDescripcion());
        paquete.setVueloId(dto.getVueloId());
        paquete.setHotelId(dto.getHotelId());
        paquete.setDestinoId(dto.getDestinoId());
        paquete.setDuracionDias(dto.getDuracionDias());
        paquete.setPrecioTotal(dto.getPrecioTotal());
        paquete.setDisponible(dto.getDisponible());
        Paquete actualizado = repository.save(paquete);
        log.info("Paquete {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando paquete con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Paquete {} eliminado", id);
    }
}