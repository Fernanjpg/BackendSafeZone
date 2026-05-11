package SafeZone.SafeZoneBackend.persistence.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

// Representa una alerta enviada por la víctima al presionar el botón de pánico
// RF-03: Captura de geolocalización GPS y entrada manual
@Container(containerName = "alertas_emergencia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaEmergencia {

    @Id
    private String id;

    // Partition key — agrupamos por víctima
    @PartitionKey
    private String victimaId;

    private String victimaNombre;
    private String victimaEmail;

    // Coordenadas GPS (pueden ser null si el navegador no las entregó)
    private Double latitud;
    private Double longitud;
    private Double precision;  // metros

    // Dirección escrita manualmente por la víctima
    private String direccionManual;

    // Mensaje libre opcional
    private String mensaje;

    // ACTIVA / ATENDIDA / RESUELTA
    private String estado;

    private Instant creadoEn;

    // Quién atendió la alerta
    private String atendidoPorId;
    private String atendidoPorNombre;
    private Instant atendidoEn;

    private Instant resueltoEn;
}
