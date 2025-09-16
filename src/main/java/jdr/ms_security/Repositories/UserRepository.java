package jdr.ms_security.Repositories;

import jdr.ms_security.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> { // Id en mongo son type string. Las consultas ya estan en MongoRepository
    // Aca se pueden hacer consultas sobre la base de datos
    @Query("{'email': ?0}")
    public User getUserByEmail(String email);
}
