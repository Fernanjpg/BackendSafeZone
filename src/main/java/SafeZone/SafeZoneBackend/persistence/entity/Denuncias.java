package SafeZone.SafeZoneBackend.persistence.entity;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.UsuarioResumen;
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
    private String usuarioid;




    private String victimaId;
    private String estado;         //PENDIENTE/EN_ESPERA/RESUELTO
    private String tipoViolencia;  //VIOLENCIAFISICA/ABUSO PSICOLOGICO/OTRO
    private String descripcion;
    private String nivelRiesgo;    //BAJO/MEDIO/ALTO
    private String direccion;
    private Boolean esAnonima;
    private Instant fechaDenuncia;

    //OBJETOS EMEBEBIDOS
    private RegionResumen region;
    private UsuarioResumen usuario;




}



