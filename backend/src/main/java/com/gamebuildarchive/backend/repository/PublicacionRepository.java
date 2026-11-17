package com.gamebuildarchive.backend.repository;

import com.gamebuildarchive.backend.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository
        extends JpaRepository<Publicacion, Long>,
                JpaSpecificationExecutor<Publicacion> {

    List<Publicacion> findByVideojuegoId(Long videojuegoId);

    List<Publicacion> findByTituloContainingIgnoreCase(String titulo);
}