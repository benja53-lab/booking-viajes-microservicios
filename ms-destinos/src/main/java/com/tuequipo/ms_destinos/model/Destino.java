package com.tuequipo.ms_destinos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "destinos")
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String idioma;

    @Column(nullable = false)
    private String moneda;

    @Column(nullable = false)
    private Boolean activo;
}