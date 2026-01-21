package com.alura.literAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroDTO {

    @JsonAlias("id")
    private Long id;

    @JsonAlias("title")
    private String titulo;

    @JsonAlias("subjects")
    private List<String> generos;

    @JsonAlias("authors")
    private List<AutorDTO> autores;

    @JsonAlias("summaries")
    private List<String> resumenes;

    @JsonAlias("download_count")
    private Integer descargas;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public List<AutorDTO> getAutores() {
        return autores;
    }

    public List<String> getResumenes() {
        return resumenes;
    }

    public Integer getDescargas() {
        return descargas;
    }
}
