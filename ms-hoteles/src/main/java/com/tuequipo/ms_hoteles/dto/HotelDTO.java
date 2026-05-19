package com.tuequipo.ms_hoteles.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HotelDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    @NotNull(message = "Las estrellas son obligatorias")
    @Min(value = 1, message = "Minimo 1 estrella")
    @Max(value = 5, message = "Maximo 5 estrellas")
    private Integer estrellas;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precioPorNoche;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;
}