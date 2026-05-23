package SafeZone.SafeZoneBackend.domain.dto;

import lombok.Data;

@Data
public class ViolenciaRequest {

    private String tipoViolencia;
    private String descripcion;
    private String nivelRiesgo;
    private String direccion;
}