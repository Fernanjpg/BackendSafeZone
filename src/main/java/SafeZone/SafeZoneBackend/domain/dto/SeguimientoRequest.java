package SafeZone.SafeZoneBackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SeguimientoRequest {
    @NotBlank(message = "El ID de la denuncia es obligatorio")
    private String denunciaid;

    private String profesionalid;

    @NotBlank(message = "El tipo de seguimiento es obligatorio")
    private String tipo;

    @NotBlank(message = "Las notas son obligatorias")
    private String notas;

    private String estadoanterior;
    private String estadonuevo;
}
