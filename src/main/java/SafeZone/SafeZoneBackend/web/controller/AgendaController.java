package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.service.AgendaService;
import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping
    public ResponseEntity<Agenda> crearCita(@RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.guardarEvento(agenda));
    }

    @GetMapping("/{usuarioid}")
    public ResponseEntity<List<Agenda>> listarAgenda(
            @PathVariable String usuarioid,
            @RequestParam String start, // Recibimos el String crudo con 'Z' de FullCalendar
            @RequestParam String end) {

        // Convertimos el string ISO con 'Z' de manera segura a LocalDateTime local
        LocalDateTime fechaInicio = OffsetDateTime.parse(start).toLocalDateTime();
        LocalDateTime fechaFin = OffsetDateTime.parse(end).toLocalDateTime();

        return ResponseEntity.ok(agendaService.obtenerAgenda(usuarioid, fechaInicio, fechaFin));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Agenda> actualizarEstado(
            @PathVariable String id,
            @RequestParam String usuarioid,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(agendaService.actualizarEstadoEvento(id, usuarioid, nuevoEstado));
    }

    @PatchMapping("/{id}/fechas")
    public ResponseEntity<Agenda> actualizarFechas(
            @PathVariable String id,
            @RequestParam String usuarioid,
            @RequestBody Map<String, String> body) {

        String fechaInicioStr = body.get("fechaInicio");
        String fechaFinStr = body.get("fechaFin");

        if (fechaInicioStr == null || fechaFinStr == null) {
            return ResponseEntity.badRequest().build();
        }

        // Mapea con soporte nativo de strings ISO transmitidos desde JavaScript
        LocalDateTime fechaInicio = OffsetDateTime.parse(fechaInicioStr).toLocalDateTime();
        LocalDateTime fechaFin = OffsetDateTime.parse(fechaFinStr).toLocalDateTime();

        if (fechaInicio.isAfter(fechaFin)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(agendaService.actualizarFechasEvento(id, usuarioid, fechaInicio, fechaFin));
    }
}