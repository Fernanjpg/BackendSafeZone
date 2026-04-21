package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.service.RegionesService;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regiones")
public class RegionesController {

    @Autowired
    private RegionesService regionesService;

    @GetMapping("/listar")
    public ResponseEntity<List<Regiones>> obtenerTodas() {
        return ResponseEntity.ok(regionesService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Regiones> crear(@RequestBody Regiones region) {
        return ResponseEntity.ok(regionesService.crear(region));
    }


}
