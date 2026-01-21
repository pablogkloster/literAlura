package com.alura.literAlura.service;

import com.alura.literAlura.repository.LibroRepository;
import com.alura.literAlura.ui.MenuEstado;
import org.springframework.stereotype.Service;

@Service
public class MenuEstadoService {

    private final LibroRepository libroRepository;
    private long totalDisponibles = 0;

    public MenuEstadoService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public void actualizarTotalDisponibles(long total) {
        this.totalDisponibles = total;
    }

    public MenuEstado obtenerEstado() {
        return new MenuEstado(
                totalDisponibles,
                libroRepository.count()
        );
    }
}
