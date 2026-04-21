package SafeZone.SafeZoneBackend.persistence.entity;


import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Container(containerName = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuarios {

@Id
@PartitionKey
private String id;

private String nombre;
private String apellido;
private String email;
private String password;
private String telefono;
private String roles;  // VICTIMA/PSICOLOGO/DEFENSOR LEGAL
private String estado;
private RegionResumen region;
@CreatedDate
private Instant fecharegistro;


}
