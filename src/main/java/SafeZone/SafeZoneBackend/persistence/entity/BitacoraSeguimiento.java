package SafeZone.SafeZoneBackend.persistence.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Container(containerName = "bitacora_seguimiento")
public class BitacoraSeguimiento {

    @Id
    private String id;

    @PartitionKey
    private String denunciaId;

    private String especialistaId;
    private String rolEspecialista; // PSICOLOGO o DEFENSOR
    private String nota;
    private LocalDateTime fechaRegistro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDenunciaId() {
        return denunciaId;
    }

    public void setDenunciaId(String denunciaId) {
        this.denunciaId = denunciaId;
    }

    public String getEspecialistaId() {
        return especialistaId;
    }

    public void setEspecialistaId(String especialistaId) {
        this.especialistaId = especialistaId;
    }

    public String getRolEspecialista() {
        return rolEspecialista;
    }

    public void setRolEspecialista(String rolEspecialista) {
        this.rolEspecialista = rolEspecialista;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}