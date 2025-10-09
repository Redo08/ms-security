package jdr.ms_security.Repositories;

import jdr.ms_security.Models.Permission;
import jdr.ms_security.Models.RequestedPermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RequestedPermissionRepository extends MongoRepository<RequestedPermission,String> {
    @Query("{ 'url': ?0, 'method': ?1, 'model': ?2 }")
    Optional<RequestedPermission> findByUrlAndMethodAndModel(String url, String method, String model);
}
