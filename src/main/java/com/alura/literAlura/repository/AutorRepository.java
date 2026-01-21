package com.alura.literAlura.repository;

import com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("""
        select distinct a
        from Autor a
        join fetch a.libros
        order by a.nombres
    """)
    List<Autor> findAllConLibros();

    Optional<Autor> findByNombresIgnoreCase(String nombres);

    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(
            Integer nacimiento, Integer fallecimiento
    );


}
