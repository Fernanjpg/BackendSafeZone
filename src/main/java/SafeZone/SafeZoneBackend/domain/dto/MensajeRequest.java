package SafeZone.SafeZoneBackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MensajeRequest {

    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;

    // Opcional: si viene vacío, el service lo resuelve automáticamente
    // según los participantes de la denuncia
    private String destinatarioid;
}