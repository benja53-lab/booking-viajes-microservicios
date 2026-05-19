package com.tuequipo.ms_usuarios.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser valido")
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;

    @NotBlank(message = "La nacionalidad es obligatoria")
    private String nacionalidad;
}