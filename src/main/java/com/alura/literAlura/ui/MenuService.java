package com.alura.literAlura.ui;

import com.alura.literAlura.service.BusquedaService;
import com.alura.literAlura.service.BibliotecaService;
import com.alura.literAlura.service.MenuEstadoService;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class MenuService {

    private final BusquedaService busquedaService;
    private final BibliotecaService bibliotecaService;
    private final MenuEstadoService menuEstadoService;
    private final Scanner scanner = new Scanner(System.in);

    public MenuService(
            BusquedaService busquedaService,
            BibliotecaService bibliotecaService,
            MenuEstadoService menuEstadoService
    ) {
        this.busquedaService = busquedaService;
        this.bibliotecaService = bibliotecaService;
        this.menuEstadoService = menuEstadoService;
    }

    public void mostrarMenu() {
        int opcion = -1;

        while (opcion != 0) {
            var estado = menuEstadoService.obtenerEstado();

            System.out.printf(
                    """
                    === LiterAlura ===
                    1 - Buscar nuevos libros
                    2 - Buscar en biblioteca (Guardados: %d)
                    
                    0 - Salir
                    %n""",
                    estado.totalGuardados()
            );

            System.out.println("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> buscarNuevosLibros();
                case 2 -> buscarEnBiblioteca();
                case 0 -> System.out.println("Hasta luego");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void buscarNuevosLibros() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                    === Buscar nuevos libros ===
                    1 - Buscar por título
                    2 - Buscar por autor
                    3 - Buscar por género
                    4 - Buscar por idioma
                    5 - Buscar por año
                    6 - Top 10 más descargados
                    
                    0 - Volver
                    """);

            System.out.println("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> busquedaService.buscarPorTitulo(scanner);
                case 2 -> busquedaService.buscarPorAutor(scanner);
                case 3 -> busquedaService.buscarPorGenero(scanner);
                case 4 -> busquedaService.buscarPorIdioma(scanner);
                case 5 -> busquedaService.buscarPorAnio(scanner);
                case 6 -> busquedaService.buscarTop10();
                case 0 -> System.out.println("↩ Volviendo...");
                default -> System.out.println("❌ Opción inválida");
            }
        }
    }

    private void buscarEnBiblioteca() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                    === Biblioteca ===
                    1 - Mostrar todos
                    2 - Buscar por autor
                    
                    0 - Volver
                    """);

            System.out.println("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> bibliotecaService.mostrarTodos();
                case 2 -> bibliotecaService.buscarPorAutor(scanner);
                case 0 -> System.out.println("↩ Volviendo...");
                default -> System.out.println("❌ Opción inválida");
            }
        }
    }
}
