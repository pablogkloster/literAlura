package com.alura.literAlura.service;

import com.alura.literAlura.client.GutendexClient;
import com.alura.literAlura.dto.AutorDTO;
import com.alura.literAlura.dto.GutendexRespuestaDTO;
import com.alura.literAlura.dto.LibroDTO;
import com.alura.literAlura.model.Autor;
import com.alura.literAlura.model.Libro;
import com.alura.literAlura.repository.AutorRepository;
import com.alura.literAlura.repository.LibroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class BusquedaServiceImpl implements BusquedaService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final TranslationService translationService;

    private final GutendexClient gutendexClient = new GutendexClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BusquedaServiceImpl(
            LibroRepository libroRepository,
            AutorRepository autorRepository,
            TranslationService translationService
    ) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.translationService = translationService;
    }

    // =========================
    // BÚSQUEDA POR TÍTULO
    // =========================

    @Override
    public void buscarPorTitulo(Scanner scanner) {
        try {
            System.out.print("Ingrese el título del libro (en español): ");
            String tituloEs = scanner.nextLine();

            String tituloEn = translationService.traducir(tituloEs, "es", "en");

            String json = gutendexClient.buscarPorTexto(tituloEn);

            if (json == null || json.isBlank()) {
                System.out.println("No se encontraron libros");
                return;
            }

            GutendexRespuestaDTO respuesta =
                    objectMapper.readValue(json, GutendexRespuestaDTO.class);

            if (respuesta.getLibros().isEmpty()) {
                System.out.println("No se encontraron libros");
                return;
            }

            for (LibroDTO libroDTO : respuesta.getLibros()) {
                boolean continuar = mostrarYGuardar(libroDTO, scanner);
                if (!continuar) {
                    break; // vuelve al menú anterior
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
    }

    // =========================
    // MÉTODO CENTRAL
    // =========================

    private boolean mostrarYGuardar(LibroDTO libroDTO, Scanner scanner) {

        String tituloTraducido =
                translationService.traducir(libroDTO.getTitulo(), "en", "es");

        String generoTraducido =
                translationService.traducir(libroDTO.getGeneros().stream()
                        .findFirst()
                        .orElse("Sin tema / género disponible"),"en", "es");


        String resumenOriginal = libroDTO.getResumenes().stream()
                .findFirst()
                .orElse("Sin resumen disponible");

        String resumenTraducido =
                translationService.traducir(resumenOriginal, "en", "es");

        String autor = libroDTO.getAutores().stream()
                .map(AutorDTO::getDescripcion)
                .findFirst()
                .orElse("Autor desconocido");

        System.out.println("\nTítulo: " + tituloTraducido);
        System.out.println("Autor: " + autor);
        System.out.println("Tema / género: " + generoTraducido);
        System.out.println("Resumen: " + resumenTraducido);

        System.out.print("¿Desea guardar este libro? (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();

        if (respuesta.equals("S")) {
            guardarLibro(libroDTO, tituloTraducido, generoTraducido, resumenTraducido);
            System.out.println("Libro guardado");
            return false;   // cortar búsqueda
        }

        if (respuesta.equals("N")) {
            System.out.println("Volviendo al menú anterior...\n");
            return false;  // cortar búsqueda
        }

        System.out.println("Opción inválida, se continúa con el siguiente");
        return true;
    }

    // =========================
    // GUARDADO
    // =========================

    private void guardarLibro(
            LibroDTO libroDTO,
            String tituloTraducido,
            String generoTraducido,
            String resumenTraducido) {

        if (libroRepository.findByTituloIgnoreCase(tituloTraducido).isPresent()) {
            System.out.println("El libro ya existe");
            return;
        }

        List<Autor> autores = libroDTO.getAutores().stream()
                .map(this::obtenerAutor)
                .toList();

        Libro libro = new Libro(
                tituloTraducido,
                List.of(generoTraducido),
                resumenTraducido,
                libroDTO.getDescargas(),
                autores
        );

        libroRepository.save(libro);
    }

    private Autor obtenerAutor(AutorDTO dto) {
        return autorRepository.findByNombresIgnoreCase(dto.getNombre())
                .orElseGet(() ->
                        autorRepository.save(
                                new Autor(
                                        dto.getNombre(),
                                        dto.getNacimiento(),
                                        dto.getFallecimiento()
                                )
                        )
                );
    }

    private void mostrarListadoConSeleccion(
            List<LibroDTO> libros,
            Scanner scanner
    ) {

        if (libros.isEmpty()) {
            System.out.println("No hay libros para mostrar.");
            return;
        }

        System.out.println("\nResultados encontrados:\n");

        int i = 1;
        for (LibroDTO libro : libros) {
            String autor = libro.getAutores().isEmpty()
                    ? "Autor desconocido"
                    : libro.getAutores().get(0).getNombre();

            System.out.printf("%d. %s — %s%n", i++, libro.getTitulo(), autor);
        }

        System.out.print("\nSeleccione un libro (0 para volver): ");
        int opcion;

        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida.");
            return;
        }

        if (opcion == 0) {
            System.out.println("Volviendo al menú anterior...");
            return;
        }

        if (opcion > 0 && opcion <= libros.size()) {
            mostrarYGuardar(libros.get(opcion - 1), scanner);
        } else {
            System.out.println("Opción fuera de rango.");
        }
    }


    @Override
    public void buscarPorAutor(Scanner scanner) {

        System.out.print("Ingrese el nombre del autor: ");
        String autorBuscado = scanner.nextLine();

        try {
            String json = gutendexClient.buscarPorTexto(autorBuscado);

            GutendexRespuestaDTO respuesta =
                    objectMapper.readValue(json, GutendexRespuestaDTO.class);

            if (respuesta.getLibros().isEmpty()) {
                System.out.println("No se encontraron libros para ese autor.");
                return;
            }

            // 🔁 Eliminar duplicados
            Map<String, LibroDTO> librosUnicos = new LinkedHashMap<>();

            for (LibroDTO libro : respuesta.getLibros()) {
                String autor = libro.getAutores().isEmpty()
                        ? "Desconocido"
                        : libro.getAutores().get(0).getNombre();

                String clave = libro.getTitulo() + "|" + autor;
                librosUnicos.putIfAbsent(clave, libro);
            }

            List<LibroDTO> librosFiltrados = librosUnicos.values()
                    .stream()
                    .limit(5)
                    .toList();

            mostrarListadoConSeleccion(librosFiltrados, scanner);

        } catch (Exception e) {
            System.out.println("Error al buscar libros por autor: " + e.getMessage());
        }
    }

    @Override
    public void buscarPorGenero(Scanner scanner) {
        try {
            System.out.print("Ingrese el género (en español): ");
            String generoEs = scanner.nextLine();

            String generoEn = translationService.traducir(
                    generoEs, "es", "en"
            );

            String json = gutendexClient.buscarPorTexto(generoEn);

            GutendexRespuestaDTO respuesta =
                    objectMapper.readValue(json, GutendexRespuestaDTO.class);

            // 🔹 Quitar duplicados (título + autor)
            Map<String, LibroDTO> librosUnicos = new LinkedHashMap<>();

            for (LibroDTO libro : respuesta.getLibros()) {
                String autor = libro.getAutores().isEmpty()
                        ? "Desconocido"
                        : libro.getAutores().get(0).getNombre();

                String clave = libro.getTitulo() + "|" + autor;
                librosUnicos.putIfAbsent(clave, libro);
            }

            // 🔹 Limitar resultados
            List<LibroDTO> librosFiltrados = librosUnicos.values()
                    .stream()
                    .limit(5)
                    .toList();

            System.out.println("\nResultados encontrados (máx. 5):\n");

            mostrarListadoConSeleccion(librosFiltrados, scanner);

        } catch (Exception e) {
            System.out.println("Error al buscar por género: " + e.getMessage());
        }
    }


    @Override
    public void buscarPorIdioma(Scanner scanner) {
        try {
            System.out.println("""
                Ingrese el idioma:
                1 - Español
                2 - Inglés
                3 - Francés
                4 - Portugués

                0 - Volver
                """);

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            String idioma;

            switch (opcion) {
                case 1 -> idioma = "es";
                case 2 -> idioma = "en";
                case 3 -> idioma = "fr";
                case 4 -> idioma = "pt";
                case 0 -> {
                    return;
                }
                default -> {
                    System.out.println("Opción inválida");
                    return;
                }
            }

            // 🔹 llamada a Gutendex
            String json = gutendexClient.buscarPorIdioma(idioma);

            GutendexRespuestaDTO respuesta =
                    objectMapper.readValue(json, GutendexRespuestaDTO.class);

            // 🔹 quitar duplicados
            Map<String, LibroDTO> librosUnicos = new LinkedHashMap<>();

            for (LibroDTO libro : respuesta.getLibros()) {
                String autor = libro.getAutores().isEmpty()
                        ? "Desconocido"
                        : libro.getAutores().get(0).getNombre();

                String clave = libro.getTitulo() + "|" + autor;
                librosUnicos.putIfAbsent(clave, libro);
            }

            List<LibroDTO> librosFiltrados = librosUnicos.values()
                    .stream()
                    .limit(5)
                    .toList();

            System.out.println("\nResultados encontrados (máx. 5):\n");

            mostrarListadoConSeleccion(librosFiltrados, scanner);

        } catch (Exception e) {
            System.out.println("Error al buscar por idioma: " + e.getMessage());
        }
    }

    @Override
    public void buscarPorAnio(Scanner scanner) {
        try {
            System.out.print("Ingrese el año: ");
            int anio = Integer.parseInt(scanner.nextLine());

            String json = gutendexClient.buscarPorAnio(anio);

            GutendexRespuestaDTO respuesta =
                    objectMapper.readValue(json, GutendexRespuestaDTO.class);

            if (respuesta.getLibros().isEmpty()) {
                System.out.println("No se encontraron libros para ese año.");
                return;
            }

            // 🔁 eliminar duplicados (título + autor)
            Map<String, LibroDTO> librosUnicos = new LinkedHashMap<>();

            for (LibroDTO libro : respuesta.getLibros()) {
                String autor = libro.getAutores().isEmpty()
                        ? "Desconocido"
                        : libro.getAutores().get(0).getNombre();

                String clave = libro.getTitulo() + "|" + autor;
                librosUnicos.putIfAbsent(clave, libro);
            }

            List<LibroDTO> librosFiltrados = librosUnicos.values()
                    .stream()
                    .limit(5)
                    .toList();

            mostrarListadoConSeleccion(librosFiltrados, scanner);

        } catch (Exception e) {
            System.out.println("Error al buscar por año: " + e.getMessage());
        }
    }



    @Override
    public void buscarTop10() {
        try {
            String json = gutendexClient.buscarTopDescargados();

            GutendexRespuestaDTO respuesta =
                    objectMapper.readValue(json, GutendexRespuestaDTO.class);

            // 🔹 quitar duplicados
            Map<String, LibroDTO> librosUnicos = new LinkedHashMap<>();

            for (LibroDTO libro : respuesta.getLibros()) {
                String autor = libro.getAutores().isEmpty()
                        ? "Desconocido"
                        : libro.getAutores().get(0).getNombre();

                String clave = libro.getTitulo() + "|" + autor;
                librosUnicos.putIfAbsent(clave, libro);
            }

            List<LibroDTO> top10 = librosUnicos.values()
                    .stream()
                    .limit(10)
                    .toList();

            System.out.println("\nTop 10 libros más descargados:\n");

            mostrarListadoConSeleccion(top10, new Scanner(System.in));

        } catch (Exception e) {
            System.out.println("Error al obtener el Top 10: " + e.getMessage());
        }
    }

    private List<LibroDTO> filtrarLibrosUnicos(
            List<LibroDTO> libros,
            int limite
    ) {
        Map<String, LibroDTO> unicos = new LinkedHashMap<>();

        for (LibroDTO libro : libros) {
            String autor = libro.getAutores().isEmpty()
                    ? "Desconocido"
                    : libro.getAutores().get(0).getNombre();

            unicos.putIfAbsent(libro.getTitulo() + "|" + autor, libro);
        }

        return unicos.values().stream()
                .limit(limite)
                .toList();
    }

    private void mostrarLibro(Libro libro) {
        System.out.println("\nTítulo: " + libro.getTitulo());

        String autores = libro.getAutores().stream()
                .map(Autor::getDescripcion)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Autor desconocido");

        System.out.println("Autor(es): " + autores);

        if (libro.getGenero() != null && !libro.getGenero().isEmpty()) {
            System.out.println("Tema / género: " + String.join(", ", libro.getGenero()));
        }

        System.out.println("Resumen: " + libro.getResumen());
    }



}
