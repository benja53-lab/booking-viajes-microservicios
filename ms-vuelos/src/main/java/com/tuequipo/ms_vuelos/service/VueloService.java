package com.tuequipo.ms_vuelos.service;

import com.tuequipo.ms_vuelos.dto.VueloDTO;
import com.tuequipo.ms_vuelos.model.Vuelo;
import com.tuequipo.ms_vuelos.repository.VueloRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VueloService {

    private static final Logger log = LoggerFactory.getLogger(VueloService.class);
    private final VueloRepository repository;

    public VueloService(VueloRepository repository) {
        this.repository = repository;
    }

    public Vuelo crear(VueloDTO dto) {
        log.info("Creando vuelo de {} a {}", dto.getOrigen(), dto.getDestino());
        Vuelo vuelo = new Vuelo();
        vuelo.setOrigen(dto.getOrigen());
        vuelo.setDestino(dto.getDestino());
        vuelo.setFechaSalida(dto.getFechaSalida());
        vuelo.setFechaLlegada(dto.getFechaLlegada());
        vuelo.setAsientosDisponibles(dto.getAsientosDisponibles());
        vuelo.setPrecio(dto.getPrecio());
        vuelo.setAerolinea(dto.getAerolinea());
        vuelo.setActivo(dto.getActivo());
        Vuelo guardado = repository.save(vuelo);
        log.info("Vuelo creado con ID {}", guardado.getId());
        return guardado;
    }

    public List<Vuelo> listarTodos() {
        log.info("Listando todos los vuelos");
        return repository.findAll();
    }

    public Vuelo buscarPorId(Long id) {
        log.info("Buscando vuelo con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado con ID: " + id));
    }

    public List<Vuelo> buscarPorRuta(String origen, String destino) {
        log.info("Buscando vuelos de {} a {}", origen, destino);
        return repository.findByOrigenAndDestino(origen, destino);
    }

    public List<Vuelo> buscarActivos() {
        log.info("Buscando vuelos activos");
        return repository.findByActivo(true);
    }

    public Vuelo actualizar(Long id, VueloDTO dto) {
        log.info("Actualizando vuelo con ID {}", id);
        Vuelo vuelo = buscarPorId(id);
        vuelo.setOrigen(dto.getOrigen());
        vuelo.setDestino(dto.getDestino());
        vuelo.setFechaSalida(dto.getFechaSalida());
        vuelo.setFechaLlegada(dto.getFechaLlegada());
        vuelo.setAsientosDisponibles(dto.getAsientosDisponibles());
        vuelo.setPrecio(dto.getPrecio());
        vuelo.setAerolinea(dto.getAerolinea());
        vuelo.setActivo(dto.getActivo());
        Vuelo actualizado = repository.save(vuelo);
        log.info("Vuelo {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando vuelo con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Vuelo {} eliminado", id);
    }
}