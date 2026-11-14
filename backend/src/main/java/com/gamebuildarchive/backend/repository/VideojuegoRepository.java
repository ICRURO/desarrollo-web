package com.gamebuildarchive.backend.repository;

import com.gamebuildarchive.backend.entity.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {
    List<Videojuego> findByNombreContainingIgnoreCase(String nombre);
}
