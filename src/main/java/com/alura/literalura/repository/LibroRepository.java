package com.alura.literalura.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.alura.literalura.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    Optional<Libro> findByIdLibro(Long idLibro);
    List<Libro> findByLenguaje(String lenguaje);
    List<Libro> findTop10ByOrderByNumeroDescargasDesc();

    @Query("SELECT DISTINCT l.lenguaje from Libro l ORDER BY l.lenguaje")
    List<String> obtenerListaUnicaLenguaje();

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE a.idAutor = :id")
    List<Libro> obtenerLibrosPorAutor(Long id);
}
