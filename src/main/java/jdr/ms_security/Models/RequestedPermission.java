package jdr.ms_security.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class RequestedPermission {
    @Id
    private String _id;
    private String url;
    private String method;
    private String model;  // Por ejemplo, 'User', 'Role', 'Permission' para asignar permisos
    private int cantidad;

    public RequestedPermission() {
    }

    public RequestedPermission(String url, String method, String model, int cantidad) {
        this.url = url;
        this.method = method;
        this.model = model;
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String get_id() {
        return _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
