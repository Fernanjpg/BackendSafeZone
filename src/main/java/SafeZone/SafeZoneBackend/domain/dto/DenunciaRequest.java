package SafeZone.SafeZoneBackend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DenunciaRequest {
    private String usuarioid; // <--- AGREGA ESTA LÍNEA
    private String titulo;
    private String descripcion;
    private String tipoViolencia;
    private String nivelRiesgo;
    private String estado;
    private String direccion;
}