package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaResponse;
import SafeZone.SafeZoneBackend.domain.dto.AtenderAlertaRequest;
import SafeZone.SafeZoneBackend.domain.service.AlertaEmergenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para el módulo de alertas de emergencia.
 * <p>
 * RF-03 — Registro de ubicación: permite crear alertas con GPS automático o
 * dirección ingresada manualmente por la víctima.
 * Incluye geocodificación inversa gratuita vía Nominatim (OpenStreetMap).
 */
@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins = {"http://localhost:5173", "https://tu-frontend.com"})
public class AlertaEmergenciaController {

    @Autowired
    private AlertaEmergenciaService alertaService;

    // ── Crear alerta (botón de pánico) ───────────────────────────────────────

    /**
     * La víctima envía su ubicación al presionar el botón de pánico.
     * <p>
     * Acepta GPS (lat/lon), dirección manual, o ambas.
     * Si ninguna está presente, devuelve HTTP 400.
     * <p>
     * {@code POST /api/emergency/alerts}
     */
    @PostMapping("/alerts")
    @ResponseStatus(HttpStatus.CREATED)
    public AlertaEmergenciaResponse crear(@Valid @RequestBody AlertaEmergenciaRequest request) {
        return alertaService.crearAlerta(request);
    }

    // ── Consultas ────────────────────────────────────────────────────────────

    /**
     * Lista alertas.  Los profesionales usan {@code ?status=ACTIVA} para ver
     * solo las que requieren atención inmediata.
     * <p>
     * {@code GET /api/emergency/alerts}
     * {@code GET /api/emergency/alerts?status=ACTIVA}
     */
    @GetMapping("/alerts")
    public List<AlertaEmergenciaResponse> listar(
            @RequestParam(required = false) String status) {
        if ("ACTIVE".equalsIgnoreCase(status) || "ACTIVA".equalsIgnoreCase(status)) {
            return alertaService.obtenerActivas();
        }
        return alertaService.obtenerTodas();
    }

    /**
     * Historial de alertas de una víctima — útil para el dashboard de la víctima.
     * <p>
     * {@code GET /api/emergency/alerts/victima/{victimaId}}
     */
    @GetMapping("/alerts/victima/{victimaId}")
    public List<AlertaEmergenciaResponse> listarPorVictima(
            @PathVariable String victimaId) {
        return alertaService.obtenerPorVictima(victimaId);
    }

    // ── Cambios de estado ────────────────────────────────────────────────────

    /**
     * El profesional (psicólogo / defensor) confirma que está atendiendo la alerta.
     * <p>
     * {@code PATCH /api/emergency/alerts/{id}/attend}
     */
    @PatchMapping("/alerts/{id}/attend")
    public AlertaEmergenciaResponse atender(
            @PathVariable String id,
            @RequestBody AtenderAlertaRequest request) {
        return alertaService.atenderAlerta(id, request);
    }

    /**
     * Cierre del caso de emergencia.
     * <p>
     * {@code PATCH /api/emergency/alerts/{id}/resolve}
     */
    @PatchMapping("/alerts/{id}/resolve")
    public AlertaEmergenciaResponse resolver(@PathVariable String id) {
        return alertaService.resolverAlerta(id);
    }

    // ── Geocodificación inversa (Nominatim / OpenStreetMap) ──────────────────

    /**
     * Convierte coordenadas GPS en una dirección legible (reverse geocoding).
     * <p>
     * El frontend llama a este endpoint cuando el GPS está disponible pero
     * quiere mostrar la dirección en texto al usuario.
     * Usa Nominatim — 100 % gratuito, sin API key.
     * <p>
     * {@code GET /api/emergency/geocode?lat={latitud}&lon={longitud}}
     *
     * @param lat latitud WGS-84
     * @param lon longitud WGS-84
     * @return JSON con la dirección: {@code {"direccion": "..."}}
     */
    @GetMapping("/geocode")
    public Map<String, String> geocodificar(
            @RequestParam Double lat,
            @RequestParam Double lon) {
        String direccion = alertaService.geocodificarInverso(lat, lon);
        return Map.of("direccion", direccion);
    }
}
