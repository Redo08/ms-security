package jdr.ms_security.Services;

import jdr.ms_security.Models.Session;
import jdr.ms_security.Repositories.SessionRepository;
import jdr.ms_security.utils.TwoFATemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
public class Session2FAService {

    @Autowired
    private SessionRepository theSessionRepository;

    @Autowired
    private OptService theOtpService;

    @Autowired
    private EmailService theEmailService;

    @Autowired
    private TwoFATemplate twoFATemplate;

    /**
     * Genera un nuevo código OTP, actualiza la sesión con el código y expiración, y envía la notificación.
     * @param session La sesión a la que se le asignará el OTP. Puede ser una sesión nueva o existente.
     * @param subject El asunto del correo (ej: "Código de Verificación" o "Nuevo Código de Verificación").
     * @return La sesión actualizada y guardada.
     */
    public Session generateAndSendOtp(Session session, String subject) {
        // Generar Código y Expiración => 5 min
        String otpCode = theOtpService.generateOtp();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Date expirationDate = calendar.getTime();

        // Asignar datos a la sesión
        session.setCode2FA(otpCode);
        session.setExpiration(expirationDate);

        // Guardar la sesión actualizada
        Session savedSession = this.theSessionRepository.save(session);

        // Construir la plantilla HTML
        String emailTemplate = construirPlantilla2FA(savedSession, otpCode);

        // Enviar el Correo OTP con HTML
        theEmailService.sendEmail(
                savedSession.getUser().getEmail(),
                subject,
                emailTemplate,
                true  // is_html = true
        );

        return savedSession;
    }

    /**
     * Construye la plantilla HTML del correo 2FA reemplazando las variables.
     */
    private String construirPlantilla2FA(Session session, String otpCode) {
        String plantilla = twoFATemplate.getTemplate();
        return plantilla
                .replace("{{nombre_usuario}}", session.getUser().getName())
                .replace("{{codigo_2fa}}", otpCode);
    }
}