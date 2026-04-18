package SafeZone.SafeZoneBackend.persistence.entity;


import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Container(containerName = "seguimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seguimientos {
    @Id
    private String id;
    @PartitionKey
    private String denunciaid;

    private String profesionalid;
    private String tipo;
    private String notas;
    private String estadoanterior;
    private String estadonuevo;
    private Instant fechaActualizacion;




}
