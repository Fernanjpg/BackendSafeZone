package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.Seguimientos;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SeguimientoResponse {
    private String id;
    private String denunciaid;
    private String profesionalid;
    private String tipo;
    private String notas;
    private String estadoanterior;
    private String estadonuevo;
    private Instant fechaActualizacion;

    public static SeguimientoResponse from(Seguimientos s) {
        if (s == null) return null;
        return SeguimientoResponse.builder()
                .id(s.getId())
                .denunciaid(s.getDenunciaid())
                .profesionalid(s.getProfesionalid())
                .tipo(s.getTipo())
                .notas(s.getNotas())
                .estadoanterior(s.getEstadoanterior())
                .estadonuevo(s.getEstadonuevo())
                .fechaActualizacion(s.getFechaActualizacion())
                .build();
    }
}
