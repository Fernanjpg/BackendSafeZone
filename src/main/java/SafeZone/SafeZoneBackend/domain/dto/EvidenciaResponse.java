package SafeZone.SafeZoneBackend.domain.dto;

import java.time.LocalDateTime;

public class EvidenciaResponse {

    private String id;
    private String denunciaId;
    private String nombreArchivo;
    private String tipoArchivo;
    private String especialistaId;
    private LocalDateTime fechaSubida;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDenunciaId() { return denunciaId; }
    public void setDenunciaId(String denunciaId) { this.denunciaId = denunciaId; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }

    public String getEspecialistaId() { return especialistaId; }
    public void setEspecialistaId(String especialistaId) { this.especialistaId = especialistaId; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}