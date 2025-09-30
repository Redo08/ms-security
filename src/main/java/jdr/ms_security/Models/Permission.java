package jdr.ms_security.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Permission {
    @Id
    private String _id;
    private String url;
    private String method;
    private String model;  // Por ejemplo, 'User', 'Role', 'Permission' para asignar permisos

    public Permission(String url, String method, String model) {
        this.url = url;
        this.method = method;
        this.model = model;
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
