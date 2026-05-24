package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UsuarioResponse {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String roles;
    private String estado;
    private RegionResumen region;
    private Instant fecharegistro;
}