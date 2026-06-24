package SafeZone.SafeZoneBackend.persistence.entity;

// IMPORTANTE: Asegúrate de tener esta importación
import com.fasterxml.jackson.annotation.JsonProperty;
import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.time.Instant;

@Container(containerName = "denuncias")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Denuncias {

    @Id
    private String id;

    @PartitionKey
    @JsonProperty("usuarioid")
    private String usuarioid;

    // IMPORTANTE: Agregamos el JsonProperty en minúsculas
    @JsonProperty("psicologoid")
    private String psicologoId;

    @JsonProperty("defensorlegalid")
    private String defensorLegalId;

    @JsonProperty("asignadoporid")
    private String asignadoPorId;

    private Instant fechaAsignacion;

    private String victimaId;
    private String titulo;
    private String descripcion;
    private String estado;
    private String tipoViolencia;
    private String nivelRiesgo;
    private String direccion;
    private Instant fechaDenuncia;
}