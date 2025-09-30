package jdr.ms_security.Services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {
    @Value("${notification-service}")
    private String url;

    public void sendEmail(String to, String subject, String body, boolean is_html) {
        try {
            // JSON que enviaremos
            String jsonBody = """
                {
                  "to": `${to}`,
                  "subject": "Correo desde Java organizado con la url privada",
                  "body": "<h1>Hola </h1><p>Este correo fue enviado desde un cliente Java en IntelliJ.</p>",
                  "is_html": true
                }
                """;

            // Crear cliente
            HttpClient client = HttpClient.newHttpClient();

            // Crear request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.url+"/send-email"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Enviar y obtener respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
