package jdr.ms_security.Controllers;

import jdr.ms_security.Models.RequestedPermission;
import jdr.ms_security.Repositories.RequestedPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/requested-permissions")
public class RequestedPermissionController {

    @Autowired
    private RequestedPermissionRepository theRequestedPermissionRepository;

    /**
     * 🧩 Endpoint para listar todos los permisos solicitados (debug)
     * GET /api/requested-permissions
     */
    @GetMapping("")
    public List<RequestedPermission> findAll() {
        return this.theRequestedPermissionRepository.findAll();
    }

    /**
     * 🧠 Endpoint que recibe una lista de índices y devuelve esos elementos
     * POST /api/requested-permissions/by-indexes
     * Body esperado: [0, 2, 5]
     */
    @PostMapping("/by-indexes")
    public List<RequestedPermission> findByIndexes(@RequestBody List<Integer> indexes) {
        // Obtener todos los permisos
        List<RequestedPermission> allPermissions = this.theRequestedPermissionRepository.findAll();

        // 🔥 Ordenar de MAYOR a MENOR por 'cantidad', y luego por 'url' si hay empates
        allPermissions.sort(
                Comparator.comparingInt(RequestedPermission::getCantidad)
                        .reversed()
                        .thenComparing(RequestedPermission::getUrl)
        );

        // Acceder solo a los índices válidos
        return indexes.stream()
                .filter(i -> i >= 0 && i < allPermissions.size())
                .map(allPermissions::get)
                .collect(Collectors.toList());
    }
}
