package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.AtenderAlertaRequest;
import SafeZone.SafeZoneBackend.domain.service.AlertaEmergenciaService;
import SafeZone.SafeZoneBackend.persistence.entity.AlertaEmergencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// RF-03: Botón de pánico — endpoints para crear y gestionar alertas de emergencia
@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins = {"http://localhost:5173", "https://tu-frontend.com"})
public class AlertaEmergenciaController {

    @Autowired
    private AlertaEmergenciaService alertaService;

    // Víctima envía la alerta con su ubicación
    // Frontend: POST /api/emergency/alerts
    @PostMapping("/alerts")
    @ResponseStatus(HttpStatus.CREATED)
    public AlertaEmergencia crear(@RequestBody AlertaEmergenciaRequest request) {
        return alertaService.crearAlerta(request);
    }

    // Profesionales obtienen las alertas activas (con opción de filtrar por estado)
    // Frontend: GET /api/emergency/alerts?status=ACTIVE
    @GetMapping("/alerts")
    public List<AlertaEmergencia> listar(@RequestParam(required = false) String status) {
        if ("ACTIVE".equalsIgnoreCase(status) || "ACTIVA".equalsIgnoreCase(status)) {
            return alertaService.obtenerActivas();
        }
        return alertaService.obtenerTodas();
    }

    // Profesional marca que está atendiendo la alerta
    // Frontend: PATCH /api/emergency/alerts/{id}/attend
    @PatchMapping("/alerts/{id}/attend")
    public AlertaEmergencia atender(
            @PathVariable String id,
            @RequestBody AtenderAlertaRequest request) {
        return alertaService.atenderAlerta(id, request);
    }

    // Cierre del caso de emergencia
    // Frontend: PATCH /api/emergency/alerts/{id}/resolve
    @PatchMapping("/alerts/{id}/resolve")
    public AlertaEmergencia resolver(@PathVariable String id) {
        return alertaService.resolverAlerta(id);
    }
}
