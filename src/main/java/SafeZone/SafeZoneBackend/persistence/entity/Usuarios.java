package SafeZone.SafeZoneBackend.persistence.entity;


import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Container(containerName = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {

@Id
private String id;
@PartitionKey
private String regionid;

private String nombre;
private String apellido;
private String email;
private String password;
private String telefono;
private String rol;
private String estado;
private Instant fecharegistro;

}
