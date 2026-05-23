package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.AsignacionCasoRequest;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.domain.service.DenunciasService;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciasController {

    @Autowired
    public DenunciasService denunciasService;

    @GetMapping("/listar")
    public List<Denuncias> listar() {
        return denunciasService.listarTodas();
    }

    @GetMapping("/{id}")
    public List<Denuncias> obtenerPorId(@PathVariable String id) {
        return denunciasService.buscarPorUsuarioId(id);
    }

    @PostMapping("/guardar")
    public Denuncias guardar(@RequestBody DenunciaRequest denuncia) {
        return denunciasService.guardar(denuncia);
    }

    // RF-09: Asignar caso a especialistas
    @PatchMapping("/{id}/asignar")
    public Denuncias asignarCaso(
            @PathVariable String id,
            @RequestBody AsignacionCasoRequest request
    ) {
        return denunciasService.asignarCaso(id, request);
    }

    @DeleteMapping("/eliminar")
    public void eliminar(@PathVariable Denuncias denuncias) {
        denunciasService.eliminar(denuncias);
    }
}