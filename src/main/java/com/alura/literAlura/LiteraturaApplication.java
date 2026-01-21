package com.alura.literAlura;

import com.alura.literAlura.ui.MenuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    private final MenuService menuService;

    public LiteraturaApplication(MenuService menuService) {
        this.menuService = menuService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        menuService.mostrarMenu();
    }
}
