package com.alura.literAlura.service;

import com.alura.literAlura.model.Autor;
import com.alura.literAlura.model.Libro;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
@Transactional
public class BibliotecaServiceImpl implements BibliotecaService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public BibliotecaServiceImpl(
            LibroRepository libroRepository,
            AutorRepository autorRepository
    ) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    // =========================
    // MOSTRAR TODOS
    // =========================

    @Override
    public void mostrarTodos() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros guardados");
            return;
        }

        libros.forEach(this::mostrarLibro);
    }

    // =========================
    // FILTROS
    // =========================

    @Override
    public void buscarPorAutor(Scanner scanner) {

        List<Autor> autores = autorRepository.findAllConLibros();

        if (autores.isEmpty()) {
            System.out.println("📭 No hay autores en la biblioteca");
            return;
        }

        System.out.println("\n=== Autores disponibles ===\n");

        for (int i = 0; i < autores.size(); i++) {
            Autor autor = autores.get(i);
            int cantidadLibros = autor.getLibros().size();

            System.out.printf(
                    "%d. %s (%d libro%s)%n",
                    i + 1,
                    autor.getDescripcion(),
                    cantidadLibros,
                    cantidadLibros == 1 ? "" : "s"
            );
        }

        System.out.print("\nSeleccione un autor (0 para volver): ");

        int opcion;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida");
            return;
        }

        if (opcion == 0) {
            System.out.println("Volviendo...");
            return;
        }

        if (opcion < 1 || opcion > autores.size()) {
            System.out.println("Opción fuera de rango");
            return;
        }

        Autor autorSeleccionado = autores.get(opcion - 1);

        System.out.println("\nLibros de " + autorSeleccionado.getDescripcion() + ":\n");

        autorSeleccionado.getLibros()
                .forEach(this::mostrarLibro);
    }


    // =========================
    // MOSTRAR LIBRO
    // =========================

    private void mostrarLibro(Libro libro) {
        System.out.println("\n📘 Título: " + libro.getTitulo());

        String autores = libro.getAutores().stream()
                .map(a -> a.getDescripcion())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Autor desconocido");

        System.out.println("Autor(es): " + autores);

        if (libro.getGenero() != null && !libro.getGenero().isEmpty()) {
            System.out.println("Tema / género: " + String.join(", ", libro.getGenero()));
        }

        System.out.println("Resumen: " + libro.getResumen());

        System.out.println("----------------------------------");
    }
}
