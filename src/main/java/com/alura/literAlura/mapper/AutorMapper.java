package com.alura.literAlura.mapper;

import com.alura.literAlura.dto.AutorDTO;
import com.alura.literAlura.model.Autor;

public class AutorMapper {

    public static Autor toEntity(AutorDTO dto) {
        if (dto == null) return null;

        return new Autor(
                dto.getNombre(),
                dto.getNacimiento(),
                dto.getFallecimiento()
        );
    }
}
