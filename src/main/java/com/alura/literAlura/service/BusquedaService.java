package com.alura.literAlura.service;

import java.util.Scanner;

public interface BusquedaService {
    void buscarPorTitulo(Scanner scanner);
    void buscarPorAutor(Scanner scanner);
    void buscarPorGenero(Scanner scanner);
    void buscarPorIdioma(Scanner scanner);
    void buscarPorAnio(Scanner scanner);
    void buscarTop10();
}
