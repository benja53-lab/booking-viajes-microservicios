package com.tuequipo.ms_hoteles.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hoteles")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Integer estrellas;

    @Column(nullable = false)
    private Double precioPorNoche;

    @Column(nullable = false)
    private Boolean disponible;
}