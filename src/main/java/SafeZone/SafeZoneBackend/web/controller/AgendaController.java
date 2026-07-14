package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.service.AgendaService;
import SafeZone.SafeZoneBackend.persistence.entity.Agenda;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

        private final AgendaService agendaService;

        public AgendaController(AgendaService agendaService) {
            this.agendaService = agendaService;
        }

        // 1. CREAR EVENTO (La víctima crea su CITA_PSICOLOGICA o el Admin crea Audiencias)
        @PostMapping
        @PreAuthorize("hasAnyRole('VICTIM', 'ADMIN')")
        public ResponseEntity<Agenda> crearCita(
                @RequestBody Agenda agenda,
                Authentication authentication) {

            String rolUsuario = obtenerRolLimpio(authentication);
            Agenda guardado = agendaService.guardarEvento(agenda, rolUsuario);
            return ResponseEntity.ok(guardado);
        }

        // 🌟 2. VER MIS CITAS (Para la VÍCTIMA)
        // El frontend de la víctima solo hace un GET a /api/agenda/mis-citas-victiva
        @GetMapping("/mis-citas-victima")
        @PreAuthorize("hasRole('VICTIM')")
        public ResponseEntity<List<Agenda>> listarAgendaPorVictima(Authentication authentication) {
            // Obtenemos el ID de la víctima autenticada directamente desde su token JWT
            String victimaIdAuth = authentication.getName();
            return ResponseEntity.ok(agendaService.obtenerAgendaCompletaPorUsuario(victimaIdAuth));
        }

        // 🩺 3. VER MIS CITAS (Para el PSICÓLOGO)
        // El psicólogo hace un GET a /api/agenda/mis-citas-psicologo para ver sus pacientes asignados
        @GetMapping("/mis-citas-psicologo")
        @PreAuthorize("hasRole('PSYCHOLOGIST')")
        public ResponseEntity<List<Agenda>> listarCitasPsicologo(Authentication authentication) {
            String profesionalIdAuth = authentication.getName();
            return ResponseEntity.ok(agendaService.obtenerCitasPorPsicologo(profesionalIdAuth));
        }

        // 🛠️ 4. GESTIONAR CITA (El Psicólogo acepta/rechaza la cita pendiente de la víctima y añade el link)
        @PutMapping("/{id}/gestionar")
        @PreAuthorize("hasRole('PSYCHOLOGIST')")
        public ResponseEntity<Agenda> gestionarCita(
                @PathVariable String id,
                @RequestBody Map<String, String> body,
                Authentication authentication) {

            String profesionalIdAuth = authentication.getName();
            String nuevoEstado = body.get("estado");
            String linkReunion = body.get("linkReunion");

            if (nuevoEstado == null) {
                return ResponseEntity.badRequest().build();
            }

            Agenda actualizada = agendaService.gestionarCitaPorPsicologo(
                    id,
                    nuevoEstado,
                    linkReunion,
                    profesionalIdAuth
            );

            return ResponseEntity.ok(actualizada);
        }

        // 💡 Helper privado para limpiar los roles inyectados por Spring Security
        private String obtenerRolLimpio(Authentication authentication) {
            String rolUsuario = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_VICTIM");

            if (rolUsuario.startsWith("ROLE_")) {
                rolUsuario = rolUsuario.substring(5);
            }
            return rolUsuario;
        }
    }
