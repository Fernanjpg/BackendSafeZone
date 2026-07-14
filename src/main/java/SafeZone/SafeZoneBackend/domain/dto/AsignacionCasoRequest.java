package SafeZone.SafeZoneBackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AsignacionCasoRequest {

    @NotBlank(message = "psicologoId es obligatorio")
    private String psicologoId;
    private String defensorLegalId;
    private String asignadoPorId;
    private String prioridad;
}