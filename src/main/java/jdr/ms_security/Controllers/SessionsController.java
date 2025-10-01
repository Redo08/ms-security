package jdr.ms_security.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import jdr.ms_security.Models.Session;
import jdr.ms_security.Models.User;
import jdr.ms_security.Repositories.SessionRepository;
import jdr.ms_security.Services.EmailService;
import jdr.ms_security.Services.JwtService;
import jdr.ms_security.Services.OptService;
import jdr.ms_security.Services.Session2FAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/sessions")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class SessionsController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private SessionRepository theSessionRepository; // El objeto va a tener el comportamiento de la interfaz
    @Autowired
    private JwtService theJwtService;
    @Autowired
    private Session2FAService theSession2FAService;

    /* Uso de clases internas para Facilitar la respuesta esperada del JSON del Front */
    // Para el endpoint /verify-2fa
    public static class VerificationPayload {
        private String sessionId;
        private String code;

        public String getSessionId() {
            return sessionId;
        }
        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
    }
    // Para el endpoint /resend-otp
    public static class ResendPayload {
        private String sessionId;
        public String getSessionId() {
            return this.sessionId;
        }
        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<Session> find() {
        return this.theSessionRepository.findAll();
    }

    @GetMapping("{id}")
    public Session findById(@PathVariable String id) { // @PathVariable permite identificar el identificador que viene en la Ruta
        Session theSession = this.theSessionRepository.findById(id).orElse(null);
        return theSession;
    }

    @PostMapping
    public Session create(@RequestBody Session newSession) {  // El casteo permite que el JSON se convierta en Objeto
        return this.theSessionRepository.save(newSession);
    }

    @PutMapping("{id}")
    public Session update(@PathVariable String id, @RequestBody Session newSession) {
        Session actualSession = this.theSessionRepository.findById(id).orElse(null);
        if (actualSession != null) {
            actualSession.setExpiration(newSession.getExpiration());
            actualSession.setToken(newSession.getToken());
            actualSession.setCode2FA(newSession.getCode2FA());
            this.theSessionRepository.save(actualSession);
            return actualSession;
        } else {
            return null;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Session theSession = this.theSessionRepository.findById(id).orElse(null);
        if (theSession != null) {
            this.theSessionRepository.delete(theSession);
        }
    }


    /* METODOS PARA EL 2FA */
    @PostMapping("/verify-2fa")
    public HashMap<String, Object> verify2FA(@RequestBody VerificationPayload payload, // Usa el DTO
                                             final HttpServletResponse response) throws IOException {
        // Verificación usando el payload
        HashMap<String, Object> theResponse = new HashMap<>();

        Session pendingSession = this.theSessionRepository.findById(payload.getSessionId()).orElse(null);

        if (pendingSession == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Session ID no encontrado.");
            return theResponse;
        }

        // Verificamos codigo y expiración
        boolean isCodeValid = pendingSession.getCode2FA() != null &&
                pendingSession.getCode2FA().equals(payload.getCode());

        boolean isNotExpired = pendingSession.getExpiration() != null &&
                pendingSession.getExpiration().after(new Date());

        if ( isCodeValid && isNotExpired) {
            // Completamos la sesión con el token
            User user = pendingSession.getUser();
            String finalToken = theJwtService.generateToken(user);

            pendingSession.setToken(finalToken);
            pendingSession.setCode2FA(null);
            pendingSession.setExpiration(new Date(System.currentTimeMillis() + 86400000));
            this.theSessionRepository.save(pendingSession);

            user.setPassword("");
            theResponse.put("token", finalToken);
            theResponse.put("user", user);
            theResponse.put("sessionId", pendingSession.get_id());
            return theResponse;

        } else if (!isNotExpired) {
            this.theSessionRepository.delete(pendingSession);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Código expirado. Vuelva a iniciar sesión.");
            return theResponse;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Código 2FA incorrecto.");
            return theResponse;
        }
    }

    @PostMapping("/resend-otp")
    public HashMap<String, Object> resendOtp(@RequestBody ResendPayload payload, // Usa el DTO
                                             final HttpServletResponse response) throws IOException {
        // Renovar el codigo OTP
        HashMap<String, Object> theResponse = new HashMap<>();

        Session actualSession = this.theSessionRepository.findById(payload.getSessionId()).orElse(null);

        if (actualSession == null || actualSession.getCode2FA() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sesión no válida o ya finalizada.");
            return theResponse;
        }

        // Generar nuevo OTP, actualizar sesión y enviar Email
        theSession2FAService.generateAndSendOtp(actualSession, "Nuevo Código de Verificación 2FA");

        theResponse.put("message", "Nuevo código enviado correctamente.");
        return theResponse;
    }
}