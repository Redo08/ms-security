package jdr.ms_security.Models;

import lombok.Data; // Lombok es para verificar que todo quede bien escrito
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;  // De Mongo

@Data
@Document // Asi se llaman los registros en Bases no relacionales
public class User {
    @Id
    private String _id; // En mongo tienen que ir con _id
    private String name;
    private String email;
    private String password;
    private boolean twoFactorEnabled; // Necesario para el 2 step verification. Facilita la vida
    private String firebaseUid;


    // Constructor por defecto
    public User() {  // Polimorfismo de sobrecarga because se llama igual pero uno tiene atributos y el otro no

    }

    public User(String name, String email, String password, boolean twoFactorEnabled) {  // Sin id because Mongo lo pone automaticamente
        this.name = name;
        this.email = email;
        this.password = password;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    // Getters y setters
    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
}
