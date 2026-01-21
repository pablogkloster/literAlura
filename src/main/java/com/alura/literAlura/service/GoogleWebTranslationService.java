// Implementación basada en el endpoint público de Google Translate
// Uso educativo / desarrollo

package com.alura.literAlura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleWebTranslationService implements TranslationService {

    private static final String URL =
            "https://translate.googleapis.com/translate_a/single" +
                    "?client=gtx&dt=t";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String traducir(String texto, String origen, String destino) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }

        try {
            String encodedText = URLEncoder.encode(texto, StandardCharsets.UTF_8);

            String uri = URL +
                    "&sl=" + origen +
                    "&tl=" + destino +
                    "&q=" + encodedText;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());

            // 🔑 La traducción está en [0][0][0]
            return root.get(0).get(0).get(0).asText();

        } catch (Exception e) {
            System.err.println("Error traduciendo texto: " + e.getMessage());
            return texto; // fallback: no romper la app
        }
    }
}
