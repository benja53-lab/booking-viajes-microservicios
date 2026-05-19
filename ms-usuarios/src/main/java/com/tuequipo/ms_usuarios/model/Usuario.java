package com.tuequipo.ms_usuarios.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String nacionalidad;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private Boolean activo;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }
} 