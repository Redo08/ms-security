package jdr.ms_security.Controllers;

import io.jsonwebtoken.io.IOException;
import jdr.ms_security.Models.Profile;
import jdr.ms_security.Models.Session;
import jdr.ms_security.Models.User;
import jdr.ms_security.Repositories.ProfileRepository;
import jdr.ms_security.Repositories.SessionRepository;
import jdr.ms_security.Repositories.UserRepository;
import jdr.ms_security.Services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/users")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class UsersController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private UserRepository theUserRepository; // El objeto va a tener el comportamiento de la interfaz

    @Autowired
    private SessionRepository theSessionRepository;

    @Autowired
    private ProfileRepository theProfileRepository;

    @Autowired
    private EncryptionService theEncryptionService;

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<User> find(){
        return this.theUserRepository.findAll();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable String id){ // @PathVariable permite identificar el identificador que viene en la Ruta
        User theUser=this.theUserRepository.findById(id).orElse(null);
        return theUser;
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        // Cifrar la contraseña si no viene vacía
        if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            newUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getPassword()));
        }

        // Validar que no exista un usuario con el mismo correo
        if (theUserRepository.getUserByEmail(newUser.getEmail()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, // 409 Conflict
                    "Ya existe un usuario con el correo: " + newUser.getEmail()
            );
        }

        // Si pasa las validaciones, se guarda
        return this.theUserRepository.save(newUser);
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User newUser){
        User actualUser=this.theUserRepository.findById(id).orElse(null);
        if(actualUser!=null){
            actualUser.setName(newUser.getName());
            actualUser.setEmail(newUser.getEmail());
            actualUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getPassword()));
            this.theUserRepository.save(actualUser);
            return actualUser;
        }else{
            return null;
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        User theUser=this.theUserRepository.findById(id).orElse(null);
        if (theUser!=null){
            this.theUserRepository.delete(theUser);
        }
    }
    /* ASOCIACIÓN RELACIONES */
    // Para la asociación, el endpoint
    @PutMapping("{idUser}/session/{idSession}")
    public void matchSession(@PathVariable String idUser, @PathVariable String idSession) {
        // Primero encontramos la sesion y el usuario
        Session theSession = this.theSessionRepository.findById(idSession).orElse(null);
        User theUser = this.theUserRepository.findById(idUser).orElse(null);

        // Verificamos que si exista
        if (theSession != null && theUser != null) {
            // Asignamos el usuario a la sesión y guardamos
            theSession.setUser(theUser);
            this.theSessionRepository.save(theSession);
        }
    }
    /* Para poder hacer la 1-1 en mongo toca validar que ya no exista una referencia apuntando. */
    @PutMapping("{idUser}/profile/{idProfile}")
    public void matchProfile(@PathVariable String idUser, @PathVariable String idProfile) {
        // Primero verificamos que exista el perfil y el usuario
        Profile theProfile = this.theProfileRepository.findById(idProfile).orElse(null);
        User theUser = this.theUserRepository.findById(idUser).orElse(null);

        if (theProfile != null && theUser != null) {
            // Como es 1-1 tenemos que verificar que ya no exista un perfil asociado al usuario.
            Profile existingProfile = this.theProfileRepository.findByUser(theUser);
            if (existingProfile != null) {
                throw new RuntimeException("No se puede, ya un perfil asociado a este usuario");
            }
            // Hacemos la asignación
            theProfile.setUser(theUser);
            this.theProfileRepository.save(theProfile);
        }
    }

    // Para la desasociació seria exactamente igual solo que en el set se le guarda un valor null.
}