package jdr.ms_security.Services;

import jakarta.servlet.http.HttpServletRequest;
import jdr.ms_security.Models.*;
import jdr.ms_security.Repositories.PermissionRepository;
import jdr.ms_security.Repositories.RolePermissionRepository;
import jdr.ms_security.Repositories.UserRepository;
import jdr.ms_security.Repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidatorsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private PermissionRepository thePermissionRepository;

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private RolePermissionRepository theRolePermissionRepository;

    @Autowired
    private UserRoleRepository theUserRoleRepository;

    private static final String BEARER_PREFIX = "Bearer ";
    // Firebase tokens son generalmente más largos (>500 caracteres)
    // JWT del backend son más cortos (~200-400 caracteres)
    private static final int FIREBASE_TOKEN_MIN_LENGTH = 500;

    public boolean validationRolePermission(HttpServletRequest request,
                                            String url,
                                            String method){
        boolean success = false;
        User theUser = this.getUser(request);
        System.out.println("Usuario:" + theUser);

        if(theUser != null){
            System.out.println("Antes URL " + url + " metodo " + method);
            url = url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
            System.out.println("URL " + url + " metodo " + method);
            Permission thePermission = this.thePermissionRepository.getPermission(url, method);

            List<UserRole> roles = this.theUserRoleRepository.getRolesByUser(theUser.get_id());
            int i = 0;
            while(i < roles.size() && !success){
                UserRole actual = roles.get(i);
                Role theRole = actual.getRole();
                if(theRole != null && thePermission != null){
                    System.out.println("Rol " + theRole.get_id() + " Permission " + thePermission.get_id());
                    RolePermission theRolePermission = this.theRolePermissionRepository
                            .getRolePermission(theRole.get_id(), thePermission.get_id());
                    if (theRolePermission != null){
                        System.out.println("Funciono!");
                        success = true;
                    }
                } else {
                    success = false;
                }
                i += 1;
            }
        }
        return success;
    }

    public User getUser(final HttpServletRequest request) {
        User theUser = null;
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Header " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            System.out.println("Token length: " + token.length());

            // Determinar el tipo de token por su longitud
            if (token.length() >= FIREBASE_TOKEN_MIN_LENGTH) {
                // Token de Firebase
                System.out.println("Procesando token de Firebase");
                theUser = getUserFromFirebaseToken(token);
            } else {
                // Token JWT del backend
                System.out.println("Procesando token JWT del backend");
                theUser = getUserFromBackendToken(token);
            }
        }
        return theUser;
    }

    private User getUserFromBackendToken(String token) {
        try {
            User theUserFromToken = jwtService.getUserFromToken(token);
            if(theUserFromToken != null) {
                return this.theUserRepository.findById(theUserFromToken.get_id())
                        .orElse(null);
            }
        } catch (Exception e) {
            System.err.println("Error procesando token del backend: " + e.getMessage());
        }
        return null;
    }

    private User getUserFromFirebaseToken(String token) {
        try {
            // Validar y obtener email del token de Firebase
            String email = firebaseService.verifyTokenAndGetEmail(token);

            if(email != null && !email.isEmpty()) {
                System.out.println("Email obtenido de Firebase: " + email);
                // Buscar usuario por email en MongoDB
                return this.theUserRepository.getUserByEmail(email);
            }
        } catch (Exception e) {
            System.err.println("Error procesando token de Firebase: " + e.getMessage());
        }
        return null;
    }
}