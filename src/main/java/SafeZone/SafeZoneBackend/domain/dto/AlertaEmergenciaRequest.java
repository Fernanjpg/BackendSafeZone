package SafeZone.SafeZoneBackend.domain.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Payload que envía el frontend cuando la víctima presiona el botón de pánico.
 * <p>
 * RF-03: el sistema acepta ubicación automática (GPS) y/o dirección manual.
 * Al menos una de las dos formas DEBE estar presente.
 */
@Data
public class AlertaEmergenciaRequest {

    @NotBlank(message = "El ID de la víctima es obligatorio")
    private String victimaId;

    @NotBlank(message = "El nombre de la víctima es obligatorio")
    private String victimaNombre;

    private String victimaEmail;

    // ── Coordenadas GPS ──────────────────────────────────────────────────────
    // Ambas deben venir juntas; si el navegador negó el permiso o falló, quedan null.
    private Double latitud;
    private Double longitud;

    // Precisión de la señal GPS en metros (opcional, mejora la trazabilidad)
    private Double precision;

    // ── Dirección manual ────────────────────────────────────────────────────
    // La víctima la escribe cuando el GPS no está disponible.
    private String direccionManual;

    // ── Mensaje libre ───────────────────────────────────────────────────────
    private String mensaje;

    /**
     * Validación RF-03: la alerta debe tener al menos una forma de ubicación.
     * <ul>
     *   <li>GPS completo (latitud + longitud presentes), o</li>
     *   <li>Dirección manual no vacía.</li>
     * </ul>
     *
     * @return {@code true} si existe al menos una fuente de ubicación válida
     */
    @AssertTrue(message = "Debe proporcionar coordenadas GPS o una dirección manual")
    public boolean isUbicacionValida() {
        boolean tieneGps      = latitud != null && longitud != null;
        boolean tieneManual   = direccionManual != null && !direccionManual.isBlank();
        return tieneGps || tieneManual;
    }
}
