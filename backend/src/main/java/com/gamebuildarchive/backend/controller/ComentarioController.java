package com.gamebuildarchive.backend.controller;

import com.gamebuildarchive.backend.entity.Comentario;
import com.gamebuildarchive.backend.repository.ComentarioRepository;
import com.gamebuildarchive.backend.repository.PublicacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {

    private final ComentarioRepository comentarioRepository;
    private final PublicacionRepository publicacionRepository;

    public ComentarioController(ComentarioRepository comentarioRepository, PublicacionRepository publicacionRepository) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> listarComentarios(
            @RequestParam(required = false) Long publicacionId) {
        if (publicacionId != null) {
            return ResponseEntity.ok(comentarioRepository.findByPublicacionId(publicacionId));
        }
        return ResponseEntity.ok(comentarioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Comentario> crearComentario(@RequestBody Comentario comentario) {
        if (comentario.getPublicacionId() == null || comentario.getUsuarioId() == null ||
            comentario.getContenido() == null || comentario.getContenido().isBlank()) {
            return ResponseEntity.badRequest().build(); // HTTP 400 Bad Request
        }

        if (!publicacionRepository.existsById(comentario.getPublicacionId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }

        Comentario guardado = comentarioRepository.save(comentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> actualizarComentario(@PathVariable Long id, @RequestBody Comentario detalles) {
        if (detalles.getContenido() == null || detalles.getContenido().isBlank()) {
            return ResponseEntity.badRequest().build(); 
        }

        Optional<Comentario> existente = comentarioRepository.findById(id);
        if (existente.isPresent()) {
            Comentario comentario = existente.get();
            comentario.setContenido(detalles.getContenido());
            return ResponseEntity.ok(comentarioRepository.save(comentario));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        if (comentarioRepository.existsById(id)) {
            comentarioRepository.deleteById(id);
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }
}
