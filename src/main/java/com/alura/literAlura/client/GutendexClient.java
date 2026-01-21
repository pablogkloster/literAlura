package com.alura.literAlura.client;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class GutendexClient {

    private static final String BASE_URL = "https://gutendex.com/books/";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public String buscarPorTexto(String texto)
            throws IOException, InterruptedException {

        String query = URLEncoder.encode(texto, StandardCharsets.UTF_8);
        URI uri = URI.create(BASE_URL + "?search=" + query);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("User-Agent", "LiterAlura/1.0")
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String buscarPorIdioma(String idioma)
            throws IOException, InterruptedException {

        URI uri = URI.create(BASE_URL + "?languages=" + idioma);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String buscarTopDescargados()
            throws IOException, InterruptedException {

        URI uri = URI.create(BASE_URL);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String buscarPorAnio(int anio)
            throws IOException, InterruptedException {

        URI uri = URI.create(
                BASE_URL +
                        "?author_year_start=" + anio +
                        "&author_year_end=" + anio
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }



}
