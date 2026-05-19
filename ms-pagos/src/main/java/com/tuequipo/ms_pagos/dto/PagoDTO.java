package com.tuequipo.ms_pagos.dto;

import com.tuequipo.ms_pagos.model.Pago;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PagoDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID de reserva es obligatorio")
    private Long reservaId;

    @NotNull(message = "El tipo de reserva es obligatorio")
    private Pago.TipoReserva tipoReserva;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "El metodo de pago es obligatorio")
    private Pago.MetodoPago metodoPago;
}