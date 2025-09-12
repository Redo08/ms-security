package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Profile;
import jdr.ms_security.Repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@CrossOrigin  // Permite que nos conectemos desde la misma maquina para hacer pruebas
@RestController // Definiciones de API de tipo Rest
@RequestMapping("/api/profiles")  // Esta definiendo la URL, la ruta con la que se va a activar esta clase.
public class ProfilesController {
    @Autowired // Se aplica el principio de Inyección, no tenemos que instanciar el objeto. Estos lo crean
    private ProfileRepository theProfileRepository; // El objeto va a tener el comportamiento de la interfaz

    @GetMapping("") // Cuando sea GET se activan estos metodos
    public List<Profile> find() {
        return this.theProfileRepository.findAll();
    }

    @GetMapping("{id}")
    public Profile findById(@PathVariable String id) { // @PathVariable permite identificar el identificador que viene en la Ruta
        Profile theProfile = this.theProfileRepository.findById(id).orElse(null);
        return theProfile;
    }

    @PostMapping
    public Profile create(@RequestBody Profile newProfile) {  // El casteo permite que el JSON se convierta en Objeto
        return this.theProfileRepository.save(newProfile);
    }

    @PutMapping("{id}")
    public Profile update(@PathVariable String id, @RequestBody Profile newProfile) {
        Profile actualProfile = this.theProfileRepository.findById(id).orElse(null);
        if (actualProfile != null) {
            actualProfile.setPhone(newProfile.getPhone());
            actualProfile.setPhoto(newProfile.getPhoto());
            // Aqui tendriamos que actualizar el usuario ??
            this.theProfileRepository.save(actualProfile);
            return actualProfile;
        } else {
            return null;
        }
    }

    // Para subir la foto hay 2 formas, la forma esta que es sencilla y forma pro con GridFS
    @PutMapping("{id}/photoUpload")
    public Profile photoUpload(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        Profile profile = this.theProfileRepository.findById(id).orElse(null);
        if (profile != null) {
            // Convertir archivo a base 64
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            profile.setPhoto(base64Image);  // Con esto basta para mandarlo al front, solo con get.

            this.theProfileRepository.save(profile);

            return profile;
        } else {
            throw new IOException("Perfil no encontrado");
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Profile theProfile = this.theProfileRepository.findById(id).orElse(null);
        if (theProfile != null) {
            this.theProfileRepository.delete(theProfile);
        }
    }

}