package SafeZone.SafeZoneBackend.persistence.entity;


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
    private String regionid;



    private String victimaId;
    private String estado;
    private String tipoViolencia;
    private String descripcion;
    private String nivelRiesgo;
    private String direccion;
    private Boolean esAnonima;
    private Instant fechaDenuncia;

    // Objetos embebidos (no son @Container)
    private VictimaResumen victima;
    private RegionResumen region;
}

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
 class VictimaResumen {
    private String nombre;
    private String telefono;
}

@Data @Builder @NoArgsConstructor @AllArgsConstructor
 class RegionResumen {
    private String nombre;
    private String codigoPostal;
}









