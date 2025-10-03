package jdr.ms_security.Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import jdr.ms_security.Models.FirebaseUserInfo;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    /**
     * Verifica el token de Firebase y retorna el email del usuario
     * Si no hay email, retorna el nombre o UID como alternativa
     * @param token Token de Firebase a verificar
     * @return Email del usuario, o identificador alternativo
     */
    public String verifyTokenAndGetEmail(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();


            // Si el email no está en el token, intentar obtenerlo del usuario
            if (email == null || email.isEmpty()) {
                email = getEmailFromFirebaseUser(uid);
            }

            // También revisar claims personalizados por si hay email ahí
            if ((email == null || email.isEmpty()) && decodedToken.getClaims().containsKey("email")) {
                email = (String) decodedToken.getClaims().get("email");
                System.out.println("   - Email obtenido de claims: " + email);
            }

            System.out.println("✅ Email final: " + email);
            return email;

        } catch (Exception e) {
            System.err.println("❌ Error verificando token de Firebase: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae información del usuario del token de Firebase
     * Retorna un objeto con email, name, uid, picture
     * @param token Token de Firebase
     * @return FirebaseUserInfo con los datos del usuario
     */
    public FirebaseUserInfo getUserInfoFromToken(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();

            // Intentar obtener email de Firebase Auth si no está en el token
            if (email == null || email.isEmpty()) {
                email = getEmailFromFirebaseUser(uid);
            }

            return new FirebaseUserInfo(uid, email, name, picture);

        } catch (Exception e) {
            System.err.println("❌ Error obteniendo info del token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene el email consultando directamente el usuario en Firebase Auth usando su UID
     * Equivalente a: admin.auth().getUser(uid)
     * @param uid UID del usuario en Firebase
     * @return Email del usuario o null si no se encuentra
     */
    private String getEmailFromFirebaseUser(String uid) {
        try {


            // Equivalente a: admin.auth().getUser(uid)
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

            String email = userRecord.getEmail();
            String displayName = userRecord.getDisplayName();
            String photoUrl = userRecord.getPhotoUrl();
            boolean emailVerified = userRecord.isEmailVerified();


            // Si el email principal es null, buscar en los providerData
            if ((email == null || email.isEmpty()) && userRecord.getProviderData() != null) {

                for (com.google.firebase.auth.UserInfo providerData : userRecord.getProviderData()) {
                    String providerEmail = providerData.getEmail();
                    String providerId = providerData.getProviderId();



                    // Si encontramos un email en algún proveedor, usarlo
                    if (providerEmail != null && !providerEmail.isEmpty()) {
                        email = providerEmail;
                        System.out.println("      ✅ Email encontrado en proveedor " + providerId + ": " + email);
                        break;
                    }
                }
            }

            if (email == null || email.isEmpty()) {
                System.err.println("      ⚠️ El usuario NO tiene email en Firebase ni en proveedores");
                System.err.println("      💡 Opciones:");
                System.err.println("         1. Actualizar el usuario en Firebase Console");
                System.err.println("         2. Solicitar permisos de email en el frontend");
                System.err.println("         3. Usar otro identificador (UID o nombre)");
            }

            return email;

        } catch (com.google.firebase.auth.FirebaseAuthException e) {
            System.err.println("   ❌ FirebaseAuthException: " + e.getMessage());
            System.err.println("   ❌ Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("   ❌ Error obteniendo usuario de Firebase Auth: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene el email del usuario de Firebase dado su UID
     * Método público para usar desde otros servicios
     * @param uid UID del usuario en Firebase
     * @return Email del usuario
     */
    public String getEmailByFirebaseUid(String uid) {
        return getEmailFromFirebaseUser(uid);
    }

    /**
     * Verifica si el token de Firebase es válido
     * @param token Token a verificar
     * @return true si es válido, false en caso contrario
     */
    public boolean isValidFirebaseToken(String token) {
        try {
            FirebaseAuth.getInstance().verifyIdToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene información completa del token para debugging
     * @param token Token de Firebase
     */
    public void debugToken(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            System.out.println("\n🔍 DEBUG - Información completa del token:");
            System.out.println("   - UID: " + decodedToken.getUid());
            System.out.println("   - Email: " + decodedToken.getEmail());
            System.out.println("   - Name: " + decodedToken.getName());
            System.out.println("   - Picture: " + decodedToken.getPicture());
            System.out.println("   - Issuer: " + decodedToken.getIssuer());
            System.out.println("   - Email Verified: " + decodedToken.isEmailVerified());
            System.out.println("   - Claims: " + decodedToken.getClaims());
            System.out.println();
        } catch (Exception e) {
            System.err.println("❌ Error en debug: " + e.getMessage());
        }
    }
}