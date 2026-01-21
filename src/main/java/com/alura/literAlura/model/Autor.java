package com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;

    private Integer nacimiento;
    private Integer fallecimiento;

    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros = new ArrayList<>();

    public Autor() {}

    public Autor(String nombre, Integer nacimiento, Integer fallecimiento) {
        this.nombres = nombre;
        this.nacimiento = nacimiento;
        this.fallecimiento = fallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public String getDescripcion() {
        String n = nacimiento != null ? nacimiento.toString() : "?";
        String f = fallecimiento != null ? fallecimiento.toString() : "?";
        return nombres + " (" + n + "–" + f + ")";
    }

    public String getNombre() {
        return nombres;
    }

    public Integer getnacimiento() {
        return nacimiento;
    }

    public Integer getfallecimiento() {
        return fallecimiento;
    }




}
