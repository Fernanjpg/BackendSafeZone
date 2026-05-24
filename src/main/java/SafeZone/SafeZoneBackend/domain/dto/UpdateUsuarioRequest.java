package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import lombok.Data;

@Data
public class UpdateUsuarioRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String roles;
    private String estado;
    private String password;
    private RegionResumen region;
}