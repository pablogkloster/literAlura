package com.alura.literAlura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexRespuestaDTO {

    @JsonAlias("count")
    private Integer total;

    @JsonAlias("results")
    private List<LibroDTO> libros;

    public int getTotal() {
        return total;
    }

    public List<LibroDTO> getLibros() {
        return libros == null ? List.of() : libros;
    }
}
