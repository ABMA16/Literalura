package com.alura.literalura.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.alura.literalura.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long>{
    List<Autor> findAllByOrderByNombreAsc();
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT f FROM Autor f WHERE f.fechaNacimiento < :fecha AND f.fechaMuerte > :fecha ORDER BY f.nombre ASC")
    List<Autor> obtenerAutorVivoFecha(int fecha);
}
