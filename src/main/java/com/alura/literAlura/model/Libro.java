package com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ElementCollection
    @CollectionTable(name = "libro_generos", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "genero")
    private List<String> generos;

    @Column(length = 5000)
    private String resumen;

    private Integer descargas;

    @ManyToMany
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    public Libro() {}

    public Libro(
            String titulo,
            List<String> generos,
            String resumen,
            Integer descargas,
            List<Autor> autores
    ) {
        this.titulo = titulo;
        this.generos = generos;
        this.resumen = resumen;
        this.descargas = descargas;
        this.autores = autores;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getGenero() {
        return generos;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public String getResumen() {
        return resumen;
    }

    public int getDescargas() {
        return descargas;
    }
}
