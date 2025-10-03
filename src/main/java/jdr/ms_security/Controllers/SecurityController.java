package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Permission;
import jdr.ms_security.Models.Session;
import jdr.ms_security.Models.User;
import jdr.ms_security.Repositories.SessionRepository;
import jdr.ms_security.Repositories.UserRepository;
import jdr.ms_security.Services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private SessionRepository theSessionRepository;
    @Autowired
    private Session2FAService theSession2FAService;
    @Autowired
    private EncryptionService theEncryptionService;
    @Autowired
    private JwtService theJwtService;

    private ValidatorsService theValidatorsService;

    @PostMapping("permissions-validation")
    public boolean permissionsValidation(final HttpServletRequest request,
                                         @RequestBody Permission thePermission) {
        boolean success=this.theValidatorsService.validationRolePermission(request,thePermission.getUrl(),thePermission.getMethod());
        return success;
    }

    /* Login tradicional (EMAIL Y CONTRASEÑA)*/
    @PostMapping("login")
    // HashMap es como un diccionario
    public HashMap<String, Object> login(@RequestBody User theNewUser,
                                         final HttpServletResponse response) throws IOException {
        HashMap<String, Object> theResponse = new HashMap<>();
        User theActualUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());

        if (theActualUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);  // Error 401 de no estar autorizado CREDENCIALES INVALIDAS
            return theResponse;
        }
        // Logica bloqueo cruzado
        if (theActualUser.getPassword() == null) {
            // Bloquear si el usuario no se registro con email/password
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Use su Login Social para acceder a esta cuenta.");
            return theResponse;
        }

        // Verificar que la contraseña coincida
        if (!theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Credenciales inválidas.");
            return theResponse;
        }

        // Credenciales validas
        if (theActualUser.isTwoFactorEnabled()) { // Verificar si el 2FA esta activado
            // Crear sesión pendiente, sin token
            Session pendingSession = new Session();
            pendingSession.setUser(theActualUser);
            // Generar OTP, Guardar y enviar email
            pendingSession = theSession2FAService.generateAndSendOtp(pendingSession, "Código de Verificación 2FA");
            // Devolvemos el Id de la sesión pendiente y el indicador 2FA
            theActualUser.setPassword("");
            theResponse.put("user", theActualUser);
            theResponse.put("requiresTwoFactor", true); // Indicamos que redirija
            theResponse.put("sessionId", pendingSession.get_id()); // Id para verficacion
            return theResponse;
        } else {
            // Login tradicional (sin 2FA)
            String token = theJwtService.generateToken(theActualUser);
            // Creamos la sesión ifnal
            Session finalSession = new Session();
            finalSession.setUser(theActualUser);
            finalSession.setToken(token);
            // Expiración basada en el JWT => 24 horas
            finalSession.setExpiration(new Date(System.currentTimeMillis() + 86400000));
            this.theSessionRepository.save(finalSession);
            theActualUser.setPassword("");
            theResponse.put("token", token);
            theResponse.put("user", theActualUser);
            theResponse.put("requiresTwoFactor", false);
            return theResponse;
        }
    }
}
