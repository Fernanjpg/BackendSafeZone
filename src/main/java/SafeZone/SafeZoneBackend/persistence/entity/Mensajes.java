package SafeZone.SafeZoneBackend.persistence.entity;


import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Container(containerName = "mensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Mensajes {

@Id
private String id;
@PartitionKey
private String denunciaid;

private String remitenteid;
private String destinatarioid;
private String contenido;
private Boolean leido;
private Instant fechaenvio;


}
