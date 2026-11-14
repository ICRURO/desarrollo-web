package com.gamebuildarchive.backend.repository;

import com.gamebuildarchive.backend.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByVideojuegoId(Long videojuegoId);

    List<Publicacion> findByTituloContainingIgnoreCase(String titulo);
}
