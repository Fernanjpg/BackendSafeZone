package SafeZone.SafeZoneBackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MensajeRequest {

    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;

    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatarioid;
}