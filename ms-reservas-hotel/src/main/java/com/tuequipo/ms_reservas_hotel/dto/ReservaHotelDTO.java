package com.tuequipo.ms_reservas_hotel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservaHotelDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID de hotel es obligatorio")
    private Long hotelId;

    @NotNull(message = "La fecha de check-in es obligatoria")
    private LocalDate fechaCheckIn;

    @NotNull(message = "La fecha de check-out es obligatoria")
    private LocalDate fechaCheckOut;

    @NotNull(message = "La cantidad de huespedes es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 huesped")
    @Max(value = 10, message = "Maximo 10 huespedes")
    private Integer cantidadHuespedes;

    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precioTotal;
}