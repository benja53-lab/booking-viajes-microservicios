package com.tuequipo.ms_resenas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private Long referenciaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoResena tipo;

    @Column(nullable = false)
    private Integer calificacion;

    @Column(nullable = false)
    private String comentario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public enum TipoResena {
        HOTEL, VUELO, PAQUETE, DESTINO
    }
}