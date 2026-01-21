package com.alura.literAlura.mapper;

import com.alura.literAlura.dto.LibroDTO;
import com.alura.literAlura.model.Autor;
import com.alura.literAlura.model.Libro;
import com.alura.literAlura.service.TranslationService;

import java.util.List;
import java.util.stream.Collectors;

public class LibroMapper {

    public static Libro toEntity(LibroDTO dto, TranslationService translator) {

        // 1️⃣ Título traducido
        String tituloEs = translator.traducir(
                dto.getTitulo(), "en", "es"
        );

        // 2️⃣ Elegir un resumen y traducirlo
        String resumenOriginal = dto.getResumenes() != null && !dto.getResumenes().isEmpty()
                ? dto.getResumenes().get(0)
                : "Sin resumen disponible";

        String resumenEs = translator.traducir(
                resumenOriginal, "en", "es"
        );

        List<String> generosEs = dto.getGeneros() == null || dto.getGeneros().isEmpty()
                ? List.of("Sin tema / género disponible")
                : dto.getGeneros().stream()
                .map(g -> translator.traducir(g, "en", "es"))
                .toList();

        // 3️⃣ Autores
        List<Autor> autores = dto.getAutores().stream()
                .map(AutorMapper::toEntity)
                .collect(Collectors.toList());

        // 4️⃣ Crear entidad
        return new Libro(
                tituloEs,
                generosEs,
                resumenEs,
                dto.getDescargas(),
                autores
        );
    }
}
