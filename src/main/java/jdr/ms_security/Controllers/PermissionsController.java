package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Permission;
import jdr.ms_security.Repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/permissions")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class PermissionsController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private PermissionRepository thePermissionRepository; // El objeto va a tener el comportamiento de la interfaz

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<Permission> find(){
        return this.thePermissionRepository.findAll();
    }
    @GetMapping("{id}")
    public Permission findById(@PathVariable String id){ // @PathVariable permite identificar el identificador que viene en la Ruta
        Permission thePermission=this.thePermissionRepository.findById(id).orElse(null);
        return thePermission;
    }
    @PostMapping
    public Permission create(@RequestBody Permission newPermission){  // El casteo permite que el JSON se convierta en Objeto
        return this.thePermissionRepository.save(newPermission);
    }
    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission newPermission){
        Permission actualPermission=this.thePermissionRepository.findById(id).orElse(null);
        if(actualPermission!=null){
            actualPermission.setMethod(newPermission.getMethod());
            actualPermission.setUrl(newPermission.getUrl());
            this.thePermissionRepository.save(actualPermission);
            return actualPermission;
        }else{
            return null;
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Permission thePermission=this.thePermissionRepository.findById(id).orElse(null);
        if (thePermission!=null){
            this.thePermissionRepository.delete(thePermission);
        }
    }
}