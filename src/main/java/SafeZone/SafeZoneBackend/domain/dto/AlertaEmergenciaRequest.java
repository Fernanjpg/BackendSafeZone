package SafeZone.SafeZoneBackend.domain.dto;

import lombok.Data;

// Lo que envía el frontend cuando la víctima presiona el botón de pánico
@Data
public class AlertaEmergenciaRequest {

    private String victimaId;
    private String victimaNombre;
    private String victimaEmail;

    // GPS — puede ser null si el navegador no lo permitió
    private Double latitud;
    private Double longitud;
    private Double precision;

    // Dirección escrita a mano por la víctima
    private String direccionManual;

    // Mensaje adicional libre
    private String mensaje;
}
