package jdr.ms_security.Repositories;

import jdr.ms_security.Models.RolePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends MongoRepository<RolePermission,String> {
    @Query("{'permission.$id': ObjectId(?0)}")  // Consulta en mongo, se dice el id del permiso.
    public List<RolePermission> getRolesByPermission(String permissionId);

    @Query("{'role.$id': ObjectId(?0)}")
    public List<RolePermission> getPermissionByRole(String roleId);

    // Corregir este
    @Query("{ 'permission.$id': ObjectId(?0), 'role.$id': ObjectId(?1) }")
    public RolePermission getPermissionByPermissionAndRole(String permissionId, String roleId);

}
