package com.gamebuildarchive.backend.controller;

import com.gamebuildarchive.backend.entity.Publicacion;
import com.gamebuildarchive.backend.repository.PublicacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin(origins = "*")
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;

    public PublicacionController(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Publicacion>> listarPublicaciones(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Long videojuegoId) {
        
        if (titulo != null && !titulo.isEmpty()) {
            return ResponseEntity.ok(publicacionRepository.findByTituloContainingIgnoreCase(titulo));
        } else if (videojuegoId != null) {
            return ResponseEntity.ok(publicacionRepository.findByVideojuegoId(videojuegoId));
        }
        return ResponseEntity.ok(publicacionRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Publicacion> crearPublicacion(@RequestBody Publicacion publicacion) {
        Publicacion guardada = publicacionRepository.save(publicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> actualizarPublicacion(@PathVariable Long id, @RequestBody Publicacion detallesPublicacion) {
        Optional<Publicacion> publicacionExistente = publicacionRepository.findById(id);
        
        if (publicacionExistente.isPresent()) {
            Publicacion publicacion = publicacionExistente.get();
            publicacion.setTitulo(detallesPublicacion.getTitulo());
            publicacion.setResumen(detallesPublicacion.getResumen());
            publicacion.setVideojuego(detallesPublicacion.getVideojuego());
            
            Publicacion actualizada = publicacionRepository.save(publicacion);
            return ResponseEntity.ok(actualizada); // HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // HTTP 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long id) {
        if (publicacionRepository.existsById(id)) {
            publicacionRepository.deleteById(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
}