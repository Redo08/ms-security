package jdr.ms_security.Services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {
    @Value("${notification-service}") // URL del microservicio notificaciones FLASK
    private String url;

    /*
    * @param to Correo de destino
    * @param subject Asunto del correo
    * @param body Contenido del correo
    * @param is_html Indica si el cuerpo es HTML
    * */
    public void sendEmail(String to, String subject, String body, boolean is_html) {
        try {
            // JSON que enviaremos
            String jsonBody = String.format("""
                    {
                    "to": "%s",
                    "subject": "%s",
                    "body": "%s",
                    "is_html": %b
                    }
                    """, to, subject.replace("\"", "\\\""), body.replace("\"", "\\\""), is_html);

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
}
