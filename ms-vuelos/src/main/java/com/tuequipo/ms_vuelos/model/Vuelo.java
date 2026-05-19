package com.tuequipo.ms_vuelos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vuelos")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDateTime fechaSalida;

    @Column(nullable = false)
    private LocalDateTime fechaLlegada;

    @Column(nullable = false)
    private Integer asientosDisponibles;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private String aerolinea;

    @Column(nullable = false)
    private Boolean activo;
}