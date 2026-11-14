package com.gamebuildarchive.backend.controller;

import com.gamebuildarchive.backend.entity.Videojuego;
import com.gamebuildarchive.backend.repository.VideojuegoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videojuegos")
@CrossOrigin(origins = "*")
public class VideojuegoController {

    private final VideojuegoRepository videojuegoRepository;

    public VideojuegoController(VideojuegoRepository videojuegoRepository) {
        this.videojuegoRepository = videojuegoRepository;
    }

    @GetMapping
    public List<Videojuego> listarVideojuegos() {
        return videojuegoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Videojuego> crearVideojuego(@RequestBody Videojuego videojuego) {
        Videojuego guardado = videojuegoRepository.save(videojuego);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}