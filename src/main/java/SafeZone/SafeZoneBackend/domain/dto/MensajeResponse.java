package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.Mensajes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeResponse {

    private String id;
    private String denunciaid;
    private String remitenteid;
    private String remitenteNombre;
    private String remitenteRol;
    private String destinatarioid;
    private String contenido;
    private Boolean leido;
    private String fechaenvio;  // ISO-8601 string

    public static MensajeResponse from(Mensajes m, String remitenteNombre, String remitenteRol) {
        MensajeResponse r = new MensajeResponse();
        r.setId(m.getId());
        r.setDenunciaid(m.getDenunciaid());
        r.setRemitenteid(m.getRemitenteid());
        r.setRemitenteNombre(remitenteNombre);
        r.setRemitenteRol(remitenteRol);
        r.setDestinatarioid(m.getDestinatarioid());
        r.setContenido(m.getContenido());
        r.setLeido(m.getLeido());
        r.setFechaenvio(m.getFechaenvio() != null ? m.getFechaenvio().toString() : null);
        return r;
    }
}