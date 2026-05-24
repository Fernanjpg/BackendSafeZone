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

    // ── Coordenadas GPS (RF-03) ────────────────────────────────────────
    private Double latitud;        // Pueden ser null si GPS no está disponible
    private Double longitud;
    private Double precision;      // en metros
    private String direccionManual; // Entrada manual cuando GPS no funciona
    /**
     * Fuente de ubicación registrada. Valores posibles:
     * "GPS" - solo GPS, "MANUAL" - solo dirección manual, "GPS_Y_MANUAL" - ambas
     */
    private String fuenteUbicacion;

    //OBJETOS EMEBEBIDOS
    private RegionResumen region;
    private UsuarioResumen usuario;




}



