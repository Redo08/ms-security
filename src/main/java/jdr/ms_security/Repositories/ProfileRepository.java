package jdr.ms_security.Repositories;

import jdr.ms_security.Models.Profile;
import jdr.ms_security.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends MongoRepository<Profile,String> {
    // Como tenemos que hacer verificación por usuario, debemos crear otro metodo para buscar por usuario
    Profile findByUser(User user); // ENtonces como usa el findBy ya se sabe que es consulta,
                                    // y el User da el tipo de atributo a buscar
}
