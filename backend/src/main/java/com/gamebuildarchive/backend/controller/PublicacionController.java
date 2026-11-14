package com.gamebuildarchive.backend.controller;

import com.gamebuildarchive.backend.entity.Publicacion;
import com.gamebuildarchive.backend.repository.PublicacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*")
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;

    public PublicacionController(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    @GetMapping
    public List<Publicacion> listarPublicaciones() {
        return publicacionRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Publicacion> crearPublicacion(@RequestBody Publicacion publicacion) {
        Publicacion guardada = publicacionRepository.save(publicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
}