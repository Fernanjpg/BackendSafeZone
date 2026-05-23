package SafeZone.SafeZoneBackend.domain.dto;

import lombok.Data;

@Data
public class AsignacionCasoRequest {

    private String psicologoId;
    private String defensorLegalId;
    private String asignadoPorId;
    private String prioridad;
}