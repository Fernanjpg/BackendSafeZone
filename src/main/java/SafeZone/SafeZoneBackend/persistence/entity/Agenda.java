package SafeZone.SafeZoneBackend.persistence.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Container(containerName ="agenda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

@Id
private String id;

@PartitionKey
private String usuarioid;

private String titulo;
private LocalDateTime fechaInicio;
private LocalDateTime fechaFin;
private String tipo; // "AUDIENCIA", "CITA_PSICOLOGICA", "PLAZO_LEGAL"
private String estado; // "PENDIENTE", "COMPLETADO", "CANCELADO"
private String descripcion;



}
