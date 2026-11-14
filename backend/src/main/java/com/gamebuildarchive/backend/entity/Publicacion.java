package com.gamebuildarchive.backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que creo la publicacion
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Videojuego al que pertenece
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "videojuego_id", nullable = false)
    private Videojuego videojuego;

    @Column(name = "titulo", nullable = false, length = 180)
    private String titulo;

    @Column(name = "resumen", columnDefinition = "TEXT")
    private String resumen;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion;

    public Publicacion() {
    }

    // Asignar fechas cuando se crea la publicacion
    @PrePersist
    public void antesDeGuardar() {
        OffsetDateTime ahora = OffsetDateTime.now(ZoneOffset.UTC);
        fechaCreacion = ahora;
        fechaActualizacion = ahora;
    }

    // Actualizar la fecha cuando se modifica
    @PreUpdate
    public void antesDeActualizar() {
        fechaActualizacion = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public OffsetDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}
