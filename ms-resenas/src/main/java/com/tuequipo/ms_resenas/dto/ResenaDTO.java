package com.tuequipo.ms_resenas.dto;

import com.tuequipo.ms_resenas.model.Resena;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ResenaDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID de referencia es obligatorio")
    private Long referenciaId;

    @NotNull(message = "El tipo de resena es obligatorio")
    private Resena.TipoResena tipo;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
}