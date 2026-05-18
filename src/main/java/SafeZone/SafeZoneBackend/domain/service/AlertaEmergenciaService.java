package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.AlertaEmergenciaRepository;
import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaResponse;
import SafeZone.SafeZoneBackend.domain.dto.AtenderAlertaRequest;
import SafeZone.SafeZoneBackend.persistence.entity.AlertaEmergencia;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Lógica de negocio para las alertas de emergencia.
 * <p>
 * RF-03: soporta ubicación por GPS automático y/o dirección manual.
 * Si el GPS falla o el usuario no da permiso, se acepta la dirección escrita.
 * La fuente queda registrada en el campo {@code fuenteUbicacion}.
 */
@Service
public class AlertaEmergenciaService {

    @Autowired
    private AlertaEmergenciaRepository alertaRepository;

    // Cliente HTTP liviano de Spring 6 — sin dependencias extra
    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ── Crear alerta ─────────────────────────────────────────────────────────

    /**
     * Procesa el botón de pánico de la víctima.
     * <p>
     * Determina la fuente de ubicación (GPS, MANUAL o ambas) y persiste la alerta.
     *
     * @param request datos enviados por el frontend
     * @return alerta creada como DTO de respuesta
     * @throws IllegalArgumentException si no viene ninguna forma de ubicación
     */
    public AlertaEmergenciaResponse crearAlerta(AlertaEmergenciaRequest request) {
        // La validación @AssertTrue del DTO ya rechaza el request antes de llegar aquí,
        // pero lo repetimos por seguridad en caso de uso programático del service.
        boolean tieneGps    = request.getLatitud() != null && request.getLongitud() != null;
        boolean tieneManual = request.getDireccionManual() != null
                              && !request.getDireccionManual().isBlank();

        if (!tieneGps && !tieneManual) {
            throw new IllegalArgumentException(
                    "RF-03: debe proporcionar coordenadas GPS o una dirección manual");
        }

        // Calcular la fuente de ubicación
        String fuenteUbicacion;
        if (tieneGps && tieneManual) {
            fuenteUbicacion = "GPS_Y_MANUAL";
        } else if (tieneGps) {
            fuenteUbicacion = "GPS";
        } else {
            fuenteUbicacion = "MANUAL";
        }

        AlertaEmergencia alerta = AlertaEmergencia.builder()
                .id(UUID.randomUUID().toString())
                .victimaId(request.getVictimaId())
                .victimaNombre(request.getVictimaNombre())
                .victimaEmail(request.getVictimaEmail())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .precision(request.getPrecision())
                .direccionManual(request.getDireccionManual())
                .fuenteUbicacion(fuenteUbicacion)
                .mensaje(request.getMensaje())
                .estado("ACTIVA")
                .creadoEn(Instant.now())
                .build();

        return toResponse(alertaRepository.guardar(alerta));
    }

    // ── Consultas ────────────────────────────────────────────────────────────

    /** Devuelve solo las alertas activas para que los profesionales las puedan atender. */
    public List<AlertaEmergenciaResponse> obtenerActivas() {
        return alertaRepository.buscarPorEstado("ACTIVA")
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** Historial completo de todas las alertas. */
    public List<AlertaEmergenciaResponse> obtenerTodas() {
        return alertaRepository.listarTodas()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** Historial de alertas de una víctima específica. */
    public List<AlertaEmergenciaResponse> obtenerPorVictima(String victimaId) {
        return alertaRepository.buscarPorVictima(victimaId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Cambios de estado ────────────────────────────────────────────────────

    /**
     * El profesional (psicólogo / defensor) marca la alerta como atendida.
     *
     * @param alertaId ID de la alerta
     * @param request  datos del profesional que atiende
     * @return alerta actualizada
     */
    public AlertaEmergenciaResponse atenderAlerta(String alertaId, AtenderAlertaRequest request) {
        AlertaEmergencia alerta = alertaRepository.buscarPorId(alertaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Alerta no encontrada con ID: " + alertaId));

        alerta.setEstado("ATENDIDA");
        alerta.setAtendidoPorId(request.getProfesionalId());
        alerta.setAtendidoPorNombre(request.getProfesionalNombre());
        alerta.setAtendidoEn(Instant.now());

        return toResponse(alertaRepository.guardar(alerta));
    }

    /**
     * Cierra el caso de emergencia — estado final.
     *
     * @param alertaId ID de la alerta a resolver
     * @return alerta actualizada
     */
    public AlertaEmergenciaResponse resolverAlerta(String alertaId) {
        AlertaEmergencia alerta = alertaRepository.buscarPorId(alertaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Alerta no encontrada con ID: " + alertaId));

        alerta.setEstado("RESUELTA");
        alerta.setResueltoEn(Instant.now());

        return toResponse(alertaRepository.guardar(alerta));
    }

    // ── Geocodificación inversa (Nominatim — gratuita, sin API key) ──────────

    /**
     * Convierte coordenadas GPS a una dirección legible usando la API de Nominatim
     * (OpenStreetMap).  No requiere API key; es 100 % gratuita.
     * <p>
     * Política de uso de Nominatim: máx. 1 solicitud/segundo y User-Agent obligatorio.
     *
     * @param latitud  latitud WGS-84
     * @param longitud longitud WGS-84
     * @return dirección en texto, o mensaje indicando que no se encontró
     */
    public String geocodificarInverso(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            throw new IllegalArgumentException(
                    "Latitud y longitud son obligatorias para la geocodificación");
        }

        try {
            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json",
                    latitud, longitud);

            String json = restClient.get()
                    .uri(url)
                    // Nominatim exige un User-Agent identificable
                    .header(HttpHeaders.USER_AGENT, "SafeZone-Backend/1.0")
                    .retrieve()
                    .body(String.class);

            JsonNode nodo = objectMapper.readTree(json);
            JsonNode displayName = nodo.get("display_name");

            if (displayName != null && !displayName.isNull()) {
                return displayName.asText();
            }
            return "Dirección no encontrada para las coordenadas proporcionadas";

        } catch (Exception e) {
            // Si Nominatim no responde, el frontend debe caer en el flujo manual
            throw new RuntimeException(
                    "No se pudo obtener la dirección desde el servicio de mapas: " + e.getMessage());
        }
    }

    // ── Mapeo entidad → DTO de respuesta ────────────────────────────────────

    private AlertaEmergenciaResponse toResponse(AlertaEmergencia alerta) {
        return AlertaEmergenciaResponse.builder()
                .id(alerta.getId())
                .victimaId(alerta.getVictimaId())
                .victimaNombre(alerta.getVictimaNombre())
                .victimaEmail(alerta.getVictimaEmail())
                .latitud(alerta.getLatitud())
                .longitud(alerta.getLongitud())
                .precision(alerta.getPrecision())
                .direccionManual(alerta.getDireccionManual())
                .fuenteUbicacion(alerta.getFuenteUbicacion())
                .mensaje(alerta.getMensaje())
                .estado(alerta.getEstado())
                .creadoEn(alerta.getCreadoEn())
                .atendidoPorId(alerta.getAtendidoPorId())
                .atendidoPorNombre(alerta.getAtendidoPorNombre())
                .atendidoEn(alerta.getAtendidoEn())
                .resueltoEn(alerta.getResueltoEn())
                .build();
    }
}
