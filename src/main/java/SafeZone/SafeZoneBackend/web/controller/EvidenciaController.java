package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.EvidenciaResponse;
import SafeZone.SafeZoneBackend.domain.service.EvidenciaService;
import SafeZone.SafeZoneBackend.persistence.entity.Evidencia;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"})
public class EvidenciaController {

    private final EvidenciaService evidenciaService;

    public EvidenciaController(EvidenciaService evidenciaService) {
        this.evidenciaService = evidenciaService;
    }

    @PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EvidenciaResponse subir(
            @RequestParam("denunciaId") String denunciaId,
            @RequestParam("especialistaId") String especialistaId,
            @RequestParam("archivo") MultipartFile archivo
    ) {
        return evidenciaService.subir(denunciaId, especialistaId, archivo);
    }

    @GetMapping("/denuncia/{denunciaId}")
    public List<EvidenciaResponse> listarPorDenuncia(@PathVariable String denunciaId) {
        return evidenciaService.listarPorDenuncia(denunciaId);
    }

    @GetMapping("/descargar/{evidenciaId}")
    public ResponseEntity<byte[]> descargar(@PathVariable String evidenciaId) {
        Evidencia evidencia = evidenciaService.buscarArchivo(evidenciaId);
        byte[] archivo = evidenciaService.descargar(evidenciaId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + evidencia.getNombreArchivo() + "\"")
                .contentType(MediaType.parseMediaType(evidencia.getTipoArchivo()))
                .body(archivo);
    }

    @DeleteMapping("/{evidenciaId}")
    public ResponseEntity<String> eliminar(@PathVariable String evidenciaId) {
        evidenciaService.eliminar(evidenciaId);
        return ResponseEntity.ok("Evidencia eliminada correctamente");
    }
}