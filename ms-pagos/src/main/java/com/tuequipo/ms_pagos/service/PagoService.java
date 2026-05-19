package com.tuequipo.ms_pagos.service;

import com.tuequipo.ms_pagos.dto.PagoDTO;
import com.tuequipo.ms_pagos.model.Pago;
import com.tuequipo.ms_pagos.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository repository;

    public PagoService(PagoRepository repository) {
        this.repository = repository;
    }

    public Pago crear(PagoDTO dto) {
        log.info("Procesando pago para reserva {} de tipo {}", dto.getReservaId(), dto.getTipoReserva());
        Pago pago = new Pago();
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setReservaId(dto.getReservaId());
        pago.setTipoReserva(dto.getTipoReserva());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        Pago guardado = repository.save(pago);
        log.info("Pago creado con ID {}", guardado.getId());
        return guardado;
    }

    public List<Pago> listarTodos() {
        log.info("Listando todos los pagos");
        return repository.findAll();
    }

    public Pago buscarPorId(Long id) {
        log.info("Buscando pago con ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    public List<Pago> buscarPorUsuario(Long usuarioId) {
        log.info("Buscando pagos del usuario {}", usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    public Pago cambiarEstado(Long id, Pago.EstadoPago nuevoEstado) {
        log.info("Cambiando estado de pago {} a {}", id, nuevoEstado);
        Pago pago = buscarPorId(id);
        pago.setEstado(nuevoEstado);
        Pago actualizado = repository.save(pago);
        log.info("Estado de pago {} actualizado a {}", id, nuevoEstado);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Eliminando pago con ID {}", id);
        buscarPorId(id);
        repository.deleteById(id);
        log.warn("Pago {} eliminado", id);
    }
}