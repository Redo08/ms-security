package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Role;
import jdr.ms_security.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/roles")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class RolesController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private RoleRepository theRoleRepository; // El objeto va a tener el comportamiento de la interfaz

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<Role> find(){
        return this.theRoleRepository.findAll();
    }
    @GetMapping("{id}")
    public Role findById(@PathVariable String id){ // @PathVariable permite identificar el identificador que viene en la Ruta
        Role theRole=this.theRoleRepository.findById(id).orElse(null);
        return theRole;
    }
    @PostMapping
    public Role create(@RequestBody Role newRole){  // El casteo permite que el JSON se convierta en Objeto
        return this.theRoleRepository.save(newRole);
    }
    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role newRole){
        Role actualRole=this.theRoleRepository.findById(id).orElse(null);
        if(actualRole!=null){
            actualRole.setName(newRole.getName());
            actualRole.setDescription(newRole.getDescription());
            this.theRoleRepository.save(actualRole);
            return actualRole;
        }else{
            return null;
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Role theRole=this.theRoleRepository.findById(id).orElse(null);
        if (theRole!=null){
            this.theRoleRepository.delete(theRole);
        }
    }
}