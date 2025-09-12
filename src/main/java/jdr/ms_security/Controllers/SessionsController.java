package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Session;
import jdr.ms_security.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/sessions")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class SessionsController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private SessionRepository theSessionRepository; // El objeto va a tener el comportamiento de la interfaz

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<Session> find(){
        return this.theSessionRepository.findAll();
    }
    @GetMapping("{id}")
    public Session findById(@PathVariable String id){ // @PathVariable permite identificar el identificador que viene en la Ruta
        Session theSession=this.theSessionRepository.findById(id).orElse(null);
        return theSession;
    }
    @PostMapping
    public Session create(@RequestBody Session newSession){  // El casteo permite que el JSON se convierta en Objeto
        return this.theSessionRepository.save(newSession);
    }
    @PutMapping("{id}")
    public Session update(@PathVariable String id, @RequestBody Session newSession){
        Session actualSession=this.theSessionRepository.findById(id).orElse(null);
        if(actualSession!=null){
            actualSession.setExpiration(newSession.getExpiration());
            actualSession.setToken(newSession.getToken());
            actualSession.setCode2FA(newSession.getCode2FA());
            this.theSessionRepository.save(actualSession);
            return actualSession;
        }else{
            return null;
        }
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Session theSession=this.theSessionRepository.findById(id).orElse(null);
        if (theSession!=null){
            this.theSessionRepository.delete(theSession);
        }
    }
}