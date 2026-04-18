package SafeZone.SafeZoneBackend.persistence.entity;


import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.security.PrivilegedAction;

@Container(containerName ="evidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor



public class Evidencias {
    @Id
    private String id;

    @PartitionKey
    private String regionid;




}
