package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;

public class DenunciaRequest {

    private String usuarioid;     // Partition Key
    private String victimaId;
    private String tipoViolencia;
    private String descripcion;
    private String nivelRiesgo;
    private String direccion;
    private Boolean esAnonima;
    private RegionResumen region;

    public DenunciaRequest() {
    }

    public DenunciaRequest(String usuarioid, String victimaId, String tipoViolencia, String descripcion, String nivelRiesgo, String direccion, Boolean esAnonima, String regionId) {
        this.usuarioid = usuarioid;
        this.victimaId = victimaId;
        this.tipoViolencia = tipoViolencia;
        this.descripcion = descripcion;
        this.nivelRiesgo = nivelRiesgo;
        this.direccion = direccion;
        this.esAnonima = esAnonima;
        this.region = region;
    }

    public String getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(String usuarioid) {
        this.usuarioid = usuarioid;
    }

    public String getVictimaId() {
        return victimaId;
    }

    public void setVictimaId(String victimaId) {
        this.victimaId = victimaId;
    }

    public String getTipoViolencia() {
        return tipoViolencia;
    }

    public void setTipoViolencia(String tipoViolencia) {
        this.tipoViolencia = tipoViolencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEsAnonima() {
        return esAnonima;
    }

    public void setEsAnonima(Boolean esAnonima) {
        this.esAnonima = esAnonima;
    }

    public RegionResumen getRegion() {
        return region;
    }

    public void setRegionId(RegionResumen region) {
        this.region = this.region;
    }
}
