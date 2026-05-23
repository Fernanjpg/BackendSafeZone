package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.ViolenciaRequest;
import SafeZone.SafeZoneBackend.domain.service.DenunciasService;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/denuncias")
public class DenunciasController {

    @Autowired
    public DenunciasService denunciasService;

    // LISTAR TODAS LAS DENUNCIAS
    @GetMapping("/listar")
    public List<Denuncias> listar() {
        return denunciasService.listarTodas();
    }

    // BUSCAR POR USUARIO ID
    @GetMapping("/{id}")
    public List<Denuncias> obtenerPorId(@PathVariable String id) {
        return denunciasService.buscarPorUsuarioId(id);
    }

    // RF-01 CREAR DENUNCIA
    @PostMapping("/guardar")
    public Denuncias guardar(@RequestBody DenunciaRequest denuncia) {
        return denunciasService.guardar(denuncia);
    }

    // RF-02 REGISTRAR INFORMACIÓN DE VIOLENCIA
    @PutMapping("/violencia/{id}")
    public Denuncias registrarViolencia(
            @PathVariable String id,
            @RequestBody ViolenciaRequest request) {

        return denunciasService.registrarViolencia(id, request);
    }

    // ELIMINAR DENUNCIA
    @DeleteMapping("/eliminar")
    public void eliminar(@RequestBody Denuncias denuncias) {
        denunciasService.eliminar(denuncias);
    }
}









