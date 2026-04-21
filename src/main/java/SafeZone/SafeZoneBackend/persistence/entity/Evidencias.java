package SafeZone.SafeZoneBackend.persistence.entity;


import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.security.PrivilegedAction;
import java.time.Instant;

@Container(containerName ="evidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor



public class Evidencias {
    @Id
    private String id;
    @PartitionKey
    private String denunciaid;
    private String url_storage;
    private String tipo_archivo;
    private Instant fecha_carga;






}
