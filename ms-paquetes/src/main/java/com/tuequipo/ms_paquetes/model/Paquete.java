package com.tuequipo.ms_paquetes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "paquetes")
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Long vueloId;

    @Column(nullable = false)
    private Long hotelId;

    @Column(nullable = false)
    private Long destinoId;

    @Column(nullable = false)
    private Integer duracionDias;

    @Column(nullable = false)
    private Double precioTotal;

    @Column(nullable = false)
    private Boolean disponible;
}