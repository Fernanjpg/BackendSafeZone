package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.SeguimientosRepository;
import SafeZone.SafeZoneBackend.domain.dto.SeguimientoRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Seguimientos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class SeguimientosService {

    @Autowired
    private SeguimientosRepository seguimientosRepository;

    @Autowired
    private DenunciasRepository denunciasRepository;

    @Autowired
    private DenunciaAccessValidator denunciaAccessValidator;

    private String getUsuarioAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void validarAccesoCaso(String denunciaId) {
        denunciaAccessValidator.validarAcceso(denunciaId, getUsuarioAutenticado());
    }

    public List<Seguimientos> buscarPorDenunciaId(String denunciaId) {
        // RF-05 — valida que el usuario autenticado tenga acceso al caso.
        validarAccesoCaso(denunciaId);
        return seguimientosRepository.buscarPorDenunciaId(denunciaId);
    }

    public Seguimientos crearSeguimiento(SeguimientoRequest request) {
        // RF-05 — valida que el usuario autenticado tenga acceso al caso antes de escribir.
        validarAccesoCaso(request.getDenunciaid());

        Denuncias denuncia = denunciasRepository.buscarPorId(request.getDenunciaid())
                .orElseThrow(() -> new IllegalArgumentException("Denuncia no encontrada con ID: " + request.getDenunciaid()));

        String estadoAnterior = denuncia.getEstado();

        if (request.getEstadonuevo() != null && !request.getEstadonuevo().isBlank()) {
            denuncia.setEstado(request.getEstadonuevo());
            denunciasRepository.guardar(denuncia);
        }

        Seguimientos seguimiento = new Seguimientos();
        seguimiento.setId(UUID.randomUUID().toString());
        seguimiento.setDenunciaid(request.getDenunciaid());
        seguimiento.setProfesionalid(request.getProfesionalid());
        seguimiento.setTipo(request.getTipo());
        seguimiento.setNotas(request.getNotas());
        seguimiento.setEstadoanterior(request.getEstadoanterior() != null && !request.getEstadoanterior().isBlank() ? request.getEstadoanterior() : estadoAnterior);
        seguimiento.setEstadonuevo(request.getEstadonuevo() != null && !request.getEstadonuevo().isBlank() ? request.getEstadonuevo() : denuncia.getEstado());
        seguimiento.setFechaActualizacion(Instant.now());

        return seguimientosRepository.guardar(seguimiento);
    }

    public void registrarHito(String denunciaId, String profesionalId, String tipo, String notas, String estadoAnterior, String estadoNuevo) {
        Seguimientos s = new Seguimientos();
        s.setId(UUID.randomUUID().toString());
        s.setDenunciaid(denunciaId);
        s.setProfesionalid(profesionalId);
        s.setTipo(tipo);
        s.setNotas(notas);
        s.setEstadoanterior(estadoAnterior);
        s.setEstadonuevo(estadoNuevo);
        s.setFechaActualizacion(Instant.now());
        seguimientosRepository.guardar(s);
    }
}
