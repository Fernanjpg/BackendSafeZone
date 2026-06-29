package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.SeguimientoRequest;
import SafeZone.SafeZoneBackend.domain.dto.SeguimientoResponse;
import SafeZone.SafeZoneBackend.domain.service.SeguimientosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
@CrossOrigin(origins = "http://localhost:5173")
public class SeguimientosController {

    private final SeguimientosService seguimientosService;

    public SeguimientosController(SeguimientosService seguimientosService) {
        this.seguimientosService = seguimientosService;
    }

    @GetMapping("/denuncia/{denunciaId}")
    public ResponseEntity<List<SeguimientoResponse>> listarPorDenuncia(@PathVariable String denunciaId) {
        List<SeguimientoResponse> responses = seguimientosService.buscarPorDenunciaId(denunciaId)
                .stream()
                .map(SeguimientoResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/guardar")
    public ResponseEntity<SeguimientoResponse> crear(@Valid @RequestBody SeguimientoRequest request) {
        SeguimientoResponse response = SeguimientoResponse.from(seguimientosService.crearSeguimiento(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}