package SafeZone.SafeZoneBackend.domain.dto;

import lombok.Data;

// Lo que el profesional manda al marcar una alerta como atendida
@Data
public class AtenderAlertaRequest {
    private String profesionalId;
    private String profesionalNombre;
}
