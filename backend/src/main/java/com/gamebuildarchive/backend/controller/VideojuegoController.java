package com.gamebuildarchive.backend.controller;

import com.gamebuildarchive.backend.entity.Videojuego;
import com.gamebuildarchive.backend.repository.VideojuegoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videojuegos")
@CrossOrigin(origins = "*")
public class VideojuegoController {

    private final VideojuegoRepository videojuegoRepository;

    public VideojuegoController(VideojuegoRepository videojuegoRepository) {
        this.videojuegoRepository = videojuegoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Videojuego>> listarVideojuegos(
            @RequestParam(required = false) String nombre) {
        
        if (nombre != null && !nombre.isEmpty()) {
            return ResponseEntity.ok(videojuegoRepository.findByNombreContainingIgnoreCase(nombre));
        }
        return ResponseEntity.ok(videojuegoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Videojuego> crearVideojuego(@RequestBody Videojuego videojuego) {
        Videojuego guardado = videojuegoRepository.save(videojuego);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Videojuego> actualizarVideojuego(@PathVariable Long id, @RequestBody Videojuego detallesVideojuego) {
        Optional<Videojuego> videojuegoExistente = videojuegoRepository.findById(id);
        
        if (videojuegoExistente.isPresent()) {
            Videojuego videojuego = videojuegoExistente.get();
            videojuego.setDescripcion(detallesVideojuego.getDescripcion());
            videojuego.setGenero(detallesVideojuego.getGenero());
            
            Videojuego actualizado = videojuegoRepository.save(videojuego);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVideojuego(@PathVariable Long id) {
        if (videojuegoRepository.existsById(id)) {
            videojuegoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}