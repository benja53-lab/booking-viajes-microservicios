package com.tuequipo.ms_vuelos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VueloDTO {

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime fechaSalida;

    @NotNull(message = "La fecha de llegada es obligatoria")
    private LocalDateTime fechaLlegada;

    @NotNull(message = "Los asientos disponibles son obligatorios")
    @Min(value = 1, message = "Debe haber al menos 1 asiento disponible")
    private Integer asientosDisponibles;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotBlank(message = "La aerolinea es obligatoria")
    private String aerolinea;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}