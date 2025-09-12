package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Permission;
import jdr.ms_security.Models.Role;
import jdr.ms_security.Models.User;
import jdr.ms_security.Models.RolePermission;
import jdr.ms_security.Repositories.PermissionRepository;
import jdr.ms_security.Repositories.RoleRepository;
import jdr.ms_security.Repositories.UserRepository;
import jdr.ms_security.Repositories.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/role-permission")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class RolePermissionController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private RolePermissionRepository theRolePermissionRepository; // El objeto va a tener el comportamiento de la interfaz

    @Autowired
    private PermissionRepository thePermissionRepository;

    @Autowired
    private RoleRepository theRoleRepository;

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<RolePermission> find(){
        return this.theRolePermissionRepository.findAll();
    }

    @GetMapping("{id}")
    public RolePermission findById(@PathVariable String id){ // @PathVariable permite identificar el identificador que viene en la Ruta
        RolePermission theRolePermission=this.theRolePermissionRepository.findById(id).orElse(null);
        return theRolePermission;
    }

    @GetMapping("permission/{permissionId}")
    public List<RolePermission> getRolesByPermission(@PathVariable String permissionId) {
        return this.theRolePermissionRepository.getRolesByPermission(permissionId); // La consulta la hace interna
    }

    @GetMapping("role/{roleId}")
    public List<RolePermission> getPermissionsByRole(@PathVariable String roleId) {
        return this.theRolePermissionRepository.getPermissionByRole(roleId);
    }

    @PostMapping("permission/{permissionId}/role/{roleId}")
    public RolePermission create(@PathVariable String permissionId, @PathVariable String roleId){  // El casteo permite que el JSON se convierta en Objeto
        // Verificación de que exista el permiso y el rol
        Permission thePermission = this.thePermissionRepository.findById(permissionId).orElse(null);
        Role theRole = this.theRoleRepository.findById(roleId).orElse(null);

        if (thePermission != null && theRole != null) {
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setPermission(thePermission);
            newRolePermission.setRole(theRole);

            return this.theRolePermissionRepository.save(newRolePermission);
        } else {
            return  null;
        }
    }

    @DeleteMapping("permission/{permissionId}/role/{roleId}")
    public void deleteByPermissionandRole(@PathVariable String permissionId, @PathVariable String roleId) {
        List<RolePermission> toDelete = this.theRolePermissionRepository.getPermissionByPermissionAndRole(permissionId, roleId);
        this.theRolePermissionRepository.deleteAll(toDelete);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        RolePermission theRolePermission=this.theRolePermissionRepository.findById(id).orElse(null);
        if (theRolePermission!=null){
            this.theRolePermissionRepository.delete(theRolePermission);
        }
    }
}