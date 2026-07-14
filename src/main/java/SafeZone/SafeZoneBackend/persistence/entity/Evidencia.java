package SafeZone.SafeZoneBackend.persistence.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Container(containerName = "evidencias")
public class Evidencia {

    @Id
    private String id;

    @PartitionKey
    private String denunciaid;

    private String nombreArchivo;
    private String tipoArchivo;
    private String archivoCifrado;
    private String especialistaId;
    private LocalDateTime fechaSubida;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDenunciaId() {
        return denunciaid;
    }

    public void setDenunciaId(String denunciaId) {
        this.denunciaid = denunciaId;
    }

    public String getDenunciaid() {
        return denunciaid;
    }

    public void setDenunciaid(String denunciaid) {
        this.denunciaid = denunciaid;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getArchivoCifrado() {
        return archivoCifrado;
    }

    public void setArchivoCifrado(String archivoCifrado) {
        this.archivoCifrado = archivoCifrado;
    }

    public String getEspecialistaId() {
        return especialistaId;
    }

    public void setEspecialistaId(String especialistaId) {
        this.especialistaId = especialistaId;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
}