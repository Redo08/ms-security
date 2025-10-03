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
            // Escapar correctamente los valores para JSON
            String jsonBody = String.format("""
                    {
                    "to": "%s",
                    "subject": "%s",
                    "body": "%s",
                    "is_html": %b
                    }
                    """,
                    escapeJson(to),
                    escapeJson(subject),
                    escapeJson(body),
                    is_html);

            // Crear cliente
            HttpClient client = HttpClient.newHttpClient();

            // Crear request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.url + "/send-email"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Enviar y obtener respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Manejo de errores
            if (response.statusCode() != 200) {
                System.err.println("Error al enviar correo (Flask Status: " + response.statusCode() + "): " + response.body());
                throw new RuntimeException("Fallo al enviar el correo a través del microservicio de notificaciones.");
            }
            System.out.println("Enviado exitosamente, Status Code: " + response.statusCode());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en la comunicación con el servicio de correos.", e);
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")  // Escape backslashes primero
                .replace("\"", "\\\"")  // Escape comillas dobles
                .replace("\n", "\\n")   // Escape saltos de línea
                .replace("\r", "\\r")   // Escape retornos de carro
                .replace("\t", "\\t");  // Escape tabulaciones
    }
}