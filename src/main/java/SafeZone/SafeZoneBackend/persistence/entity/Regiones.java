package SafeZone.SafeZoneBackend.persistence.entity;


import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Container(containerName = "regiones")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Regiones {
    @PartitionKey
    private String id;

    private String nombreRegion;
    private String codigoPostal;

}
