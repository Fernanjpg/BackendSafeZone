package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.domain.dto.AsignacionCasoRequest;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaResponse;
import SafeZone.SafeZoneBackend.domain.dto.ViolenciaRequest;
import SafeZone.SafeZoneBackend.domain.service.DenunciasService;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/denuncias")
@CrossOrigin(origins = "http://localhost:5173")
public class DenunciasController {

    @Autowired
    public DenunciasService denunciasService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/listar")
    public ResponseEntity<List<DenunciaResponse>> listar(Authentication authentication) {
        // El id de usuario SIEMPRE se extrae del JWT, nunca de un parámetro del cliente.
        String usuarioId = authentication.getName();
        Usuarios usuario = usuariosRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        String rol = usuario.getRoles() != null ? usuario.getRoles().toUpperCase() : "";

        // ADMIN puede ver la lista completa. El resto siempre se filtra por su propio id,
        // ignorando cualquier parámetro victimId de la query string.
        List<Denuncias> denuncias;
        if (rol.equals("ADMIN")) {
            denuncias = denunciasService.listarTodas();
        } else if (rol.equals("VICTIM")) {
            denuncias = denunciasService.buscarPorVictimaId(usuarioId);
        } else {
            // PSYCHOLOGIST o DEFENDER: solo sus casos asignados.
            denuncias = denunciasService.listarCasosAsignados(usuarioId, rol);
        }

        List<DenunciaResponse> response = denuncias.stream()
                .map(DenunciaResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DenunciaResponse> obtenerPorId(@PathVariable String id) {
        // Valida pertenencia/asignación antes de devolver la denuncia (RF-05).
        String usuarioId = SecurityContextHolder.getContext().getAuthentication().getName();
        Denuncias denuncia = denunciasService.obtenerPorIdConAcceso(id, usuarioId);
        return ResponseEntity.ok(DenunciaResponse.from(denuncia));
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('VICTIM')")
    public ResponseEntity<DenunciaResponse> crear(@Valid @RequestBody DenunciaRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usuarioId = authentication.getName();

        Denuncias nueva = denunciasService.crearDenuncia(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(DenunciaResponse.from(nueva));
    }

    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DenunciaResponse> asignarCaso(
            @PathVariable String id,
            @Valid @RequestBody AsignacionCasoRequest request) {
        Denuncias resultado = denunciasService.asignarCaso(id, request);
        return ResponseEntity.ok(DenunciaResponse.from(resultado));
    }

    @PatchMapping("/{id}/violencia")
    @PreAuthorize("hasRole('VICTIM')")
    public ResponseEntity<DenunciaResponse> registrarViolencia(
            @PathVariable String id,
            @RequestBody ViolenciaRequest request) {
        Denuncias resultado = denunciasService.registrarViolencia(id, request);
        return ResponseEntity.ok(DenunciaResponse.from(resultado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        denunciasService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mis-casos")
    @PreAuthorize("hasAnyRole('PSYCHOLOGIST', 'DEFENDER')")
    public ResponseEntity<List<DenunciaResponse>> listarMisCasos() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String usuarioId = authentication.getName();

        Usuarios especialista = usuariosRepository.buscarPorId(usuarioId).orElse(null);

        if (especialista == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String profesionalId = especialista.getId();
        String rol = especialista.getRoles();

        System.out.println(">>> Filtrando casos para el especialista ID: " + profesionalId + " con Rol: " + rol);

        List<Denuncias> denunciasAsignadas = denunciasService.listarCasosAsignados(profesionalId, rol);

        List<DenunciaResponse> response = denunciasAsignadas.stream()
                .map(DenunciaResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/backfill-usuarioid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> backfillUsuarioid() {
        DenunciasService.BackfillResult result = denunciasService.backfillUsuarioid();

        Map<String, Object> response = Map.of(
                "totalRevisadas", result.totalRevisadas(),
                "corregidas", result.corregidas(),
                "yaCorrectas", result.yaCorrectas(),
                "noResueltas", result.noResueltas()
        );

        return ResponseEntity.ok(response);
    }
}