package SafeZone.SafeZoneBackend.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * DTO de respuesta para alertas de emergencia.
 * <p>
 * Se usa en lugar de devolver la entidad directamente, así controlamos
 * exactamente qué datos llegan al frontend.
 * RF-03: Incluye la fuente de ubicación (GPS / MANUAL / GPS_Y_MANUAL).
 */
@Data
@Builder
public class AlertaEmergenciaResponse {

    private String id;

    // ── Datos de la víctima ──────────────────────────────────────────────────
    private String victimaId;
    private String victimaNombre;
    private String victimaEmail;

    // ── Ubicación GPS (puede ser null si el navegador no la entregó) ─────────
    private Double latitud;
    private Double longitud;
    private Double precision;           // en metros

    // ── Dirección ingresada manualmente ────────────────────────────────────
    private String direccionManual;

    /**
     * Fuente de la ubicación registrada.
     * <ul>
     *   <li>{@code "GPS"}         — solo coordenadas automáticas</li>
     *   <li>{@code "MANUAL"}      — solo dirección escrita por la víctima</li>
     *   <li>{@code "GPS_Y_MANUAL"} — ambas disponibles</li>
     * </ul>
     */
    private String fuenteUbicacion;

    // ── Mensaje libre de la víctima ─────────────────────────────────────────
    private String mensaje;

    // ── Estado del ciclo de vida ────────────────────────────────────────────
    private String estado;              // ACTIVA / ATENDIDA / RESUELTA
    private Instant creadoEn;

    // ── Atención por profesional ────────────────────────────────────────────
    private String atendidoPorId;
    private String atendidoPorNombre;
    private Instant atendidoEn;
    private Instant resueltoEn;
}
