package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository; // <-- IMPORTANTE
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
@CrossOrigin(origins = "http://localhost:5173")
public class DenunciasController {

    @Autowired
    public DenunciasService denunciasService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    // GET /api/reports?victimId=1
    @GetMapping("/listar")
    public ResponseEntity<List<DenunciaResponse>> listar(
            @RequestParam(required = false) String victimId) {

        System.out.println(">>> victimId recibido: " + victimId);
        List<Denuncias> denuncias = victimId != null
                ? denunciasService.buscarPorVictimaId(victimId)  // devuelve List
                : denunciasService.listarTodas();
        System.out.println(">>> total encontradas: " + denuncias.size());
        List<DenunciaResponse> response = denuncias.stream()
                .map(DenunciaResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    // GET /api/reports/{id}
    @GetMapping("/{id}")
    public ResponseEntity<DenunciaResponse> obtenerPorId(@PathVariable String id) {
        // 1. Obtener el usuario autenticado del contexto de seguridad
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuarios user = usuariosRepository.buscarUsuarioPorEmail(auth.getName());

        // Verificación de seguridad: si el usuario no existe en BD
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Buscar la denuncia y aplicar validación perimetral
        return denunciasService.buscarDenunciaPorId(id)
                .map(denuncia -> {
                    // Validación: ¿Es el usuario el ADMIN o el profesional asignado?
                    // Nota: Asegúrate de que el método getRoles() devuelva el String o lista correcta
                    boolean esAdmin = user.getRoles() != null && user.getRoles().contains("ADMIN");

                    boolean esPsicologo = denuncia.getPsicologoId() != null &&
                            denuncia.getPsicologoId().equals(user.getId());

                    boolean esDefensor = denuncia.getDefensorLegalId() != null &&
                            denuncia.getDefensorLegalId().equals(user.getId());

                    boolean esAsignado = esPsicologo || esDefensor;

                    if (esAdmin || esAsignado) {
                        return ResponseEntity.ok(DenunciaResponse.from(denuncia));
                    } else {
                        // RF-05: El usuario está autenticado, pero NO tiene permisos sobre este recurso específico
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<DenunciaResponse>build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/reports
    @PostMapping("/guardar")
    @PreAuthorize("hasRole('VICTIM')")
    public ResponseEntity<DenunciaResponse> crear(@Valid @RequestBody DenunciaRequest request) {
        // Quitamos el @AuthenticationPrincipal, usamos el request directo
        Denuncias nueva = denunciasService.crearDenuncia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DenunciaResponse.from(nueva));
    }

    // RF-09 ASIGANAR DENUNCIA A PROFESIONALES
    @PatchMapping("/{id}/asignar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DenunciaResponse> asignarCaso(
            @PathVariable String id,
            @RequestBody AsignacionCasoRequest request) {
        Denuncias resultado = denunciasService.asignarCaso(id, request);
        return ResponseEntity.ok(DenunciaResponse.from(resultado));
    }

    // RF-02
    @PatchMapping("/{id}/violencia")
    @PreAuthorize("hasRole('VICTIM')")
    public ResponseEntity<DenunciaResponse> registrarViolencia(
            @PathVariable String id,
            @RequestBody ViolenciaRequest request) {
        Denuncias resultado = denunciasService.registrarViolencia(id, request);
        return ResponseEntity.ok(DenunciaResponse.from(resultado));
    }

    @DeleteMapping("/eliminar")
    public void eliminar(@RequestBody Denuncias denuncias) {
        denunciasService.eliminar(denuncias);
    }

    @GetMapping("/mis-casos")
    @PreAuthorize("hasAnyRole('PSYCHOLOGIST', 'DEFENDER')")
    public ResponseEntity<List<DenunciaResponse>> listarMisCasos() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String emailOrId = authentication.getName();

        Usuarios especialista = usuariosRepository.buscarUsuarioPorEmail(emailOrId);

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
}