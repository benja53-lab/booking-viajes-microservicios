package com.tuequipo.ms_reservas_vuelo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReservaVueloDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID de vuelo es obligatorio")
    private Long vueloId;

    @NotNull(message = "La cantidad de asientos es obligatoria")
    @Min(value = 1, message = "Debe reservar al menos 1 asiento")
    @Max(value = 9, message = "Maximo 9 asientos por reserva")
    private Integer cantidadAsientos;

    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precioTotal;
}