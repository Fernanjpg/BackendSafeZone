package SafeZone.SafeZoneBackend.persistence.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
private LocalDateTime fechaInicio;
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
private LocalDateTime fechaFin;
private  String tipo;
private String estado;
private String descripcion;
private String linkReunion;
private String usuarioNombre;
private String profesionalId;
private String profesionalNombre;



}
