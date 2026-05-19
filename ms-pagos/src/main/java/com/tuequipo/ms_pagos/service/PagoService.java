package com.tuequipo.ms_pagos.service;

import com.tuequipo.ms_pagos.dto.PagoDTO;
import com.tuequipo.ms_pagos.model.Pago;
import com.tuequipo.ms_pagos.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * Servicio de negocio para pagos.
 * Verifica la reserva en el microservicio correspondiente antes de procesar el pago.
 */
@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);
    private final PagoRepository repository;
    private final ReservaClientService reservaClientService;

    public PagoService(PagoRepository repository, ReservaClientService reservaClientService) {
        this.repository = repository;
        this.reservaClientService = reservaClientService;
    }

    /**
     * Procesa un nuevo pago verificando que la reserva asociada exista.
     */
    public Pago crear(PagoDTO dto) {
        log.info("Procesando pago para reserva {} de tipo {} metodo {}",
                dto.getReservaId(), dto.getTipoReserva(), dto.getMetodoPago());

        // Regla de negocio: verificar que la reserva existe antes de aceptar el pago
        Map<String, Object> reserva = reservaClientService.verificarReserva(
                dto.getReservaId(), dto.getTipoReserva());
        String estadoReserva = (String) reserva.get("estado");
        log.info("Reserva {} encontrada con estado: {}", dto.getReservaId(), estadoReserva);

        if ("CANCELADA".equals(estadoReserva)) {
            log.warn("Intento de pago para reserva cancelada: {}", dto.getReservaId());
            throw new RuntimeException("No se puede procesar pago para una reserva CANCELADA");
        }

        Pago pago = new Pago();
        pago.setUsuarioId(dto.getUsuarioId());
        pago.setReservaId(dto.getReservaId());
        pago.setTipoReserva(dto.getTipoReserva());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        Pago guardado = repository.save(pago);
        log.info("Pago creado con ID {} por monto {}", guardado.getId(), guardado.getMonto());
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
