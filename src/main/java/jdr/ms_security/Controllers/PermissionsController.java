package jdr.ms_security.Controllers;

import jdr.ms_security.Models.Permission;

import jdr.ms_security.Models.Profile;
import jdr.ms_security.Repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/permissions")
public class PermissionsController {
    @Autowired
    private PermissionRepository thePermissionRepository;


    @GetMapping("")
    public List<Permission> findAll(){
        return this.thePermissionRepository.findAll();
    }

    @GetMapping("{id}")
    public Permission findById(@PathVariable String id) { // @PathVariable permite identificar el identificador que viene en la Ruta
        Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
        return thePermission;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission create(@RequestBody Permission theNewPermission){
        return this.thePermissionRepository.save(theNewPermission);
    }


    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission newPermission) {
        Permission actualPermission = this.thePermissionRepository.findById(id).orElse(null);
        if (actualPermission != null) {
            actualPermission.setModel(newPermission.getModel());
            actualPermission.setUrl(newPermission.getUrl());
            actualPermission.setMethod(newPermission.getMethod());
            // Aqui tendriamos que actualizar el usuario ??
            this.thePermissionRepository.save(actualPermission);
            return actualPermission;
        } else {
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Permission thePermission = this.thePermissionRepository
                .findById(id)
                .orElse(null);
        if (thePermission != null) {
            this.thePermissionRepository.delete(thePermission);
        }
    }
}
