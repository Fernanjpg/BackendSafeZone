package SafeZone.SafeZoneBackend.domain.dto;

public class BitacoraRequest {

    private String denunciaId;
    private String especialistaId;
    private String rolEspecialista;
    private String nota;

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
}