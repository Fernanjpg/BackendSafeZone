package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Valida que un usuario autenticado tenga acceso a una denuncia (caso),
 * según su rol: VICTIM solo si es dueña, PSYCHOLOGIST/DEFENDER solo si
 * están asignados, ADMIN siempre.
 *
 * Depende únicamente de los repositorios (nunca de DenunciasService ni de
 * SeguimientosService) para evitar dependencias circulares. Tanto
 * DenunciasService como SeguimientosService lo inyectan.
 */
@Service
public class DenunciaAccessValidator {

    @Autowired
    private DenunciasRepository denunciasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public void validarAcceso(String denunciaId, String usuarioId) {
        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada: " + denunciaId));
        Usuarios usuario = usuariosRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
        verificarAcceso(denuncia, usuario);
    }

    private void verificarAcceso(Denuncias denuncia, Usuarios usuario) {
        String rol = usuario.getRoles() != null ? usuario.getRoles().toUpperCase() : "";
        boolean esAdmin     = rol.equals("ADMIN");
        boolean esVictima   = usuario.getId().equals(denuncia.getVictimaId())
                || usuario.getId().equals(denuncia.getUsuarioid());
        boolean esPsicologo = usuario.getId().equals(denuncia.getPsicologoId());
        boolean esDefensor  = usuario.getId().equals(denuncia.getDefensorLegalId());

        if (!esAdmin && !esVictima && !esPsicologo && !esDefensor) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No tienes acceso a esta denuncia");
        }
    }
}
