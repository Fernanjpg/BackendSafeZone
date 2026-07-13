package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.BitacoraRequest;
import SafeZone.SafeZoneBackend.domain.service.BitacoraService;
import SafeZone.SafeZoneBackend.persistence.entity.BitacoraSeguimiento;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class BitacoraController {

    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @PostMapping("/registrar")
    public BitacoraSeguimiento registrar(@RequestBody BitacoraRequest request) {
        return bitacoraService.registrar(request);
    }

    @GetMapping("/denuncia/{denunciaId}")
    public List<BitacoraSeguimiento> listarPorDenuncia(@PathVariable String denunciaId) {
        return bitacoraService.listarPorDenuncia(denunciaId);
    }
}