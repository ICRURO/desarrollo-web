package com.gamebuildarchive.backend.entity;

import java.lang.annotation.Inherited;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Videojuegos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Videojuego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 100)
    private String genero;

    public Videojuego() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return descripcion;
    }

    public String getGenero() {
        return genero;
    }

    public String setGenero(String genero) {
        this.genero = genero;
        return genero;
    }
}