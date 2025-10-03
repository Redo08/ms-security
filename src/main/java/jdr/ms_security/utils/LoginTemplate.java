package jdr.ms_security.utils;

import org.springframework.stereotype.Component;

@Component
public class LoginTemplate {
    public String getTemplate() {
        return "<!DOCTYPE html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Nuevo inicio de sesión detectado</title>" +
                "</head>" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f7f9fc;\">" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);\">" +
                "        <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 20px; text-align: center; color: white;\">" +
                "            <div style=\"font-size: 28px; font-weight: bold; margin-bottom: 10px;\">Magic dreams tour</div>" +
                "            <h1 style=\"margin: 0; font-size: 24px;\">Nuevo inicio de sesión detectado</h1>" +
                "        </div>" +
                "        <div style=\"padding: 30px;\">" +
                "            <p style=\"font-size: 24px; color: #2d3748; margin-bottom: 20px;\">Hola, {{nombre_usuario}}</p>" +
                "            <p style=\"margin-bottom: 15px;\">Hemos detectado un nuevo inicio de sesión en tu cuenta. Si fuiste tú, puedes ignorar este mensaje. Si no reconoces esta actividad, te recomendamos cambiar tu contraseña inmediatamente.</p>" +
                "            <div style=\"background-color: #f8f9fa; border-left: 4px solid #667eea; padding: 15px; margin: 20px 0; border-radius: 0 5px 5px 0;\">" +
                "                <p style=\"margin: 0;\"><strong>Información del inicio de sesión:</strong></p>" +
                "            </div>" +
                "            <div style=\"background-color: #f1f3f4; padding: 15px; border-radius: 5px; margin: 20px 0;\">" +
                "                <div style=\"margin-bottom: 10px;\">" +
                "                    <span style=\"font-weight: bold; display: inline-block; min-width: 120px;\">Fecha y hora:</span>" +
                "                    <span>{{fecha_hora}}</span>" +
                "                </div>" +
                "            </div>" +
                "            <div style=\"background-color: #fff5f5; border: 1px solid #fed7d7; border-radius: 5px; padding: 15px; margin: 20px 0; color: #c53030;\">" +
                "                <p style=\"margin: 0;\"><strong>Nota de seguridad:</strong> Si no reconoces esta actividad, tu cuenta podría estar en riesgo. Te recomendamos cambiar tu contraseña y revisar la configuración de seguridad de tu cuenta.</p>" +
                "            </div>" +
                "            <p style=\"margin-bottom: 15px;\">Si tienes alguna pregunta o necesitas ayuda, no dudes en contactar a nuestro equipo de soporte.</p>" +
                "        </div>" +
                "        <div style=\"background-color: #f1f3f4; padding: 20px; text-align: center; font-size: 14px; color: #718096;\">" +
                "            <p style=\"margin: 5px 0;\">© 2023 Magic dreams tours. Todos los derechos reservados.</p>" +
                "            <p style=\"margin: 5px 0;\">Este es un mensaje automático, por favor no respondas a este correo.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}