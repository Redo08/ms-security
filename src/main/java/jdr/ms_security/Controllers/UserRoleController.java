package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Role;
import jdr.ms_security.Models.User;
import jdr.ms_security.Models.UserRole;
import jdr.ms_security.Repositories.RoleRepository;
import jdr.ms_security.Repositories.UserRepository;
import jdr.ms_security.Repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/user-role")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class UserRoleController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private UserRoleRepository theUserRoleRepository; // El objeto va a tener el comportamiento de la interfaz

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private RoleRepository theRoleRepository;

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<UserRole> find(){
        return this.theUserRoleRepository.findAll();
    }

    @GetMapping("{id}")
    public UserRole findById(@PathVariable String id){ // @PathVariable permite identificar el identificador que viene en la Ruta
        UserRole theUserRole=this.theUserRoleRepository.findById(id).orElse(null);
        return theUserRole;
    }

    @GetMapping("user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable String userId) {
        return this.theUserRoleRepository.getRolesByUser(userId); // La consulta la hace interna
    }

    @GetMapping("role/{roleId}")
    public List<UserRole> getUsersByRole(@PathVariable String roleId) {
        return this.theUserRoleRepository.getUsersByRole(roleId);
    }

    @PostMapping("user/{userId}/role/{roleId}")
    public UserRole create(@PathVariable String userId, @PathVariable String roleId){  // El casteo permite que el JSON se convierta en Objeto
        // Verificación de que exista el usuario y el rol
        User theUser = this.theUserRepository.findById(userId).orElse(null);
        Role theRole = this.theRoleRepository.findById(roleId).orElse(null);

        if (theUser != null && theRole != null) {
            UserRole newUserRole = new UserRole();
            newUserRole.setUser(theUser);
            newUserRole.setRole(theRole);

            return this.theUserRoleRepository.save(newUserRole);
        } else {
            return  null;
        }
    }

    @DeleteMapping("user/{userId}/role/{roleId}")
    public void deleteByUserandRole(@PathVariable String userId, @PathVariable String roleId) {
        List<UserRole> toDelete = this.theUserRoleRepository.getRolesByUserAndRole(userId, roleId);
        this.theUserRoleRepository.deleteAll(toDelete);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        UserRole theUserRole=this.theUserRoleRepository.findById(id).orElse(null);
        if (theUserRole!=null){
            this.theUserRoleRepository.delete(theUserRole);
        }
    }
}