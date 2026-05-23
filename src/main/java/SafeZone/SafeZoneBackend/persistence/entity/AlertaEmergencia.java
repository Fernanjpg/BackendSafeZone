package SafeZone.SafeZoneBackend.persistence.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

/**
 * Representa una alerta de emergencia enviada por la víctima al presionar el botón de pánico.
 * <p>
 * RF-03: registra tanto coordenadas GPS como dirección manual, y la fuente que se usó.
 */
@Container(containerName = "alertas_emergencia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaEmergencia {

    @Id
    private String id;

    /** Partition key — agrupamos alertas por víctima para consultas eficientes. */
    @PartitionKey
    private String victimaId;

    private String victimaNombre;
    private String victimaEmail;

    // ── Coordenadas GPS ──────────────────────────────────────────────────────
    // Pueden ser null si el navegador no entregó la ubicación o fue denegada.
    private Double latitud;
    private Double longitud;
    private Double precision;   // en metros

    // ── Dirección ingresada manualmente ────────────────────────────────────
    private String direccionManual;

    /**
     * Fuente de ubicación registrada. Valores posibles:
     * <ul>
     *   <li>{@code "GPS"}          — se recibieron coordenadas automáticas</li>
     *   <li>{@code "MANUAL"}       — la víctima ingresó la dirección a mano</li>
     *   <li>{@code "GPS_Y_MANUAL"} — ambas fuentes disponibles</li>
     * </ul>
     */
    private String fuenteUbicacion;

    // ── Mensaje libre de la víctima ─────────────────────────────────────────
    private String mensaje;

    // ── Estado del ciclo de vida ────────────────────────────────────────────
    /** ACTIVA → ATENDIDA → RESUELTA */
    private String estado;

    private Instant creadoEn;

    // ── Profesional que atendió ─────────────────────────────────────────────
    private String atendidoPorId;
    private String atendidoPorNombre;
    private Instant atendidoEn;

    private Instant resueltoEn;
}

