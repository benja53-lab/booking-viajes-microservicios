package com.tuequipo.ms_notificaciones.dto;

import com.tuequipo.ms_notificaciones.model.Notificacion;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NotificacionDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotNull(message = "El tipo de notificacion es obligatorio")
    private Notificacion.TipoNotificacion tipo;
}