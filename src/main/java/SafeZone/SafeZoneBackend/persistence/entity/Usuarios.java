package SafeZone.SafeZoneBackend.persistence.entity;


import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonProperty;
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

private String id;

private String nombre;

private String apellido;
@PartitionKey
@JsonProperty("email")
private String email;
@JsonProperty("password")
private String password;
private String telefono;
@JsonProperty("roles")
private String roles;
private String estado;
private RegionResumen region;
@CreatedDate
private Instant fecharegistro;


}
