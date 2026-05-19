package com.tuequipo.ms_paquetes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaqueteDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "El ID de vuelo es obligatorio")
    private Long vueloId;

    @NotNull(message = "El ID de hotel es obligatorio")
    private Long hotelId;

    @NotNull(message = "El ID de destino es obligatorio")
    private Long destinoId;

    @NotNull(message = "La duracion es obligatoria")
    @Min(value = 1, message = "La duracion minima es 1 dia")
    private Integer duracionDias;

    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precioTotal;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;
}