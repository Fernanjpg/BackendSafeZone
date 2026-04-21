package SafeZone.SafeZoneBackend.web.controller;

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

    // Listar todas las denuncias
    @GetMapping("/listar")
    public List<Denuncias> listar() {
        return denunciasService.listarTodas();
    }

    // Buscar una por UsuariosID
    @GetMapping("/{id}")
    public List<Denuncias> obtenerPorId(@PathVariable String id) {
        return denunciasService.buscarPorUsuarioId(id);
    }

    @PostMapping("/guardar")
    public Denuncias guardar(@RequestBody DenunciaRequest denuncia) {
        return denunciasService.guardar(denuncia);
    }

    // Eliminar
    @DeleteMapping("/eliminar")
    public void eliminar(@PathVariable Denuncias denuncias) {
        denunciasService.eliminar(denuncias);
    }
}















