package jdr.ms_security.Models;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

public class Session {
    private String _id;
    private String token;
    private Date expiration;
    private String code2FA;
    @DBRef // Decorador para que sepa que va a estar relacionado con el user  |Si quiero no relacional, le quito esto|
    // Mongo guarda es la referencia del objeto.
    private User user;

    // Constructor por defecto
    public Session() {

    }

    public Session(String token, Date expiration, String code2FA) {
        this.token = token;
        this.expiration = expiration;
        this.code2FA = code2FA;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getCode2FA() {
        return code2FA;
    }

    public void setCode2FA(String code2FA) {
        this.code2FA = code2FA;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
