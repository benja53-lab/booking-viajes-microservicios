package com.tuequipo.ms_destinos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DestinoDTO {

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El pais es obligatorio")
    private String pais;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message = "El idioma es obligatorio")
    private String idioma;

    @NotBlank(message = "La moneda es obligatoria")
    private String moneda;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}