package jdr.ms_security.utils;

import org.springframework.stereotype.Component;

@Component
public class TwoFATemplate {
    public String getTemplate() {
        return "<!DOCTYPE html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Código de Verificación 2FA</title>" +
                "</head>" +
                "<body style=\"font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f7f9fc;\">" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);\">" +
                "        <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 20px; text-align: center; color: white;\">" +
                "            <div style=\"font-size: 28px; font-weight: bold; margin-bottom: 10px;\">Magic dreams tours</div>" +
                "            <h1 style=\"margin: 0; font-size: 24px;\">Código de Verificación</h1>" +
                "        </div>" +
                "        <div style=\"padding: 30px;\">" +
                "            <p style=\"font-size: 24px; color: #2d3748; margin-bottom: 20px;\">Hola, {{nombre_usuario}}</p>" +
                "            <p style=\"margin-bottom: 20px;\">Has solicitado acceso a tu cuenta. Para completar el proceso de autenticación, utiliza el siguiente código de verificación:</p>" +
                "            <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 25px; text-align: center; border-radius: 10px; margin: 30px 0;\">" +
                "                <div style=\"font-size: 42px; font-weight: bold; letter-spacing: 8px; color: white; font-family: 'Courier New', monospace;\">{{codigo_2fa}}</div>" +
                "            </div>" +
                "            <div style=\"background-color: #fff5e6; border-left: 4px solid #ff9800; padding: 15px; margin: 20px 0; border-radius: 0 5px 5px 0;\">" +
                "                <p style=\"margin: 0; color: #e65100;\"><strong>⏱️ Tiempo de expiración:</strong> Este código expirará en <strong>5 minutos</strong>.</p>" +
                "            </div>" +
                "            <div style=\"background-color: #f1f3f4; padding: 15px; border-radius: 5px; margin: 20px 0;\">" +
                "                <p style=\"margin: 0 0 10px 0; font-weight: bold; color: #2d3748;\">Información importante:</p>" +
                "                <ul style=\"margin: 0; padding-left: 20px; color: #4a5568;\">" +
                "                    <li style=\"margin-bottom: 8px;\">Este código es de un solo uso</li>" +
                "                    <li style=\"margin-bottom: 8px;\">No compartas este código con nadie</li>" +
                "                    <li style=\"margin-bottom: 8px;\">Si no solicitaste este código, ignora este mensaje</li>" +
                "                </ul>" +
                "            </div>" +
                "            <div style=\"background-color: #fff5f5; border: 1px solid #fed7d7; border-radius: 5px; padding: 15px; margin: 20px 0; color: #c53030;\">" +
                "                <p style=\"margin: 0;\"><strong>🔒 Nota de seguridad:</strong> Si no reconoces esta actividad, te recomendamos cambiar tu contraseña inmediatamente y contactar a nuestro equipo de soporte.</p>" +
                "            </div>" +
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