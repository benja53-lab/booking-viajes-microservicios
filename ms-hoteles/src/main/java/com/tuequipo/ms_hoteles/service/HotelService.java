package com.tuequipo.ms_hoteles.service;

import com.tuequipo.ms_hoteles.dto.HotelDTO;
import com.tuequipo.ms_hoteles.model.Hotel;
import com.tuequipo.ms_hoteles.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelService.class);
    private final HotelRepository repository;

    public HotelService(HotelRepository repository) {
        this.repository = repository;
    }

    public Hotel crear(HotelDTO dto) {
        log.info("Creando hotel: {}", dto.getNombre());
        Hotel hotel = new Hotel();
        hotel.setNombre(dto.getNombre());
        hotel.setCiudad(dto.getCiudad());
        hotel.setDireccion(dto.getDireccion());
        hotel.setEstrellas(dto.getEstrellas());
        hotel.setPrecioPorNoche(dto.getPrecioPorNoche());
        hotel.setDisponible(dto.getDisponible());
        Hotel guardado = repository.save(hotel);
        log.info("Hotel creado con ID {}", guardado.getId());
        return guardado;
    }

    public List<Hotel> listarTodos() {
        log.info("Listando todos los hoteles");
        return repository.findAll();
    }

    public Hotel buscarPorId(Long id) {
        log.info("Buscando hotel con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado con ID: " + id));
    }

    public List<Hotel> buscarPorCiudad(String ciudad) {
        log.info("Buscando hoteles en ciudad: {}", ciudad);
        return repository.findByCiudad(ciudad);
    }

    public List<Hotel> buscarDisponibles() {
        log.info("Buscando hoteles disponibles");
        return repository.findByDisponible(true);
    }

    public Hotel actualizar(Long id, HotelDTO dto) {
        log.info("Actualizando hotel con ID {}", id);
        Hotel hotel = buscarPorId(id);
        hotel.setNombre(dto.getNombre());
        hotel.setCiudad(dto.getCiudad());
        hotel.setDireccion(dto.getDireccion());
        hotel.setEstrellas(dto.getEstrellas());
        hotel.setPrecioPorNoche(dto.getPrecioPorNoche());
        hotel.setDisponible(dto.getDisponible());
        Hotel actualizado = repository.save(hotel);
        log.info("Hotel {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando hotel con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Hotel {} eliminado", id);
    }
}