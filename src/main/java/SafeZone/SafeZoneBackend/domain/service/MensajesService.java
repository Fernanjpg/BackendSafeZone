package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.MensajesRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.domain.dto.MensajeRequest;
import SafeZone.SafeZoneBackend.domain.dto.MensajeResponse;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Mensajes;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MensajesService {

    @Autowired
    private MensajesRepository mensajesRepository;

    @Autowired
    private DenunciasRepository denunciasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    /**
     * Envía un mensaje dentro de una denuncia.
     * Si destinatarioid viene vacío/nulo, se resuelve automáticamente
     * según el rol del remitente y los participantes de la denuncia.
     */
    public MensajeResponse enviarMensaje(String denunciaid, MensajeRequest request, String remitenteEmail) {
        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada: " + denunciaid));

        Usuarios remitente = usuariosRepository.buscarUsuarioPorEmail(remitenteEmail);
        if (remitente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remitente no encontrado");
        }

        // Resolver destinatario automáticamente si no viene en el request
        String destinatarioId = resolverDestinatario(request.getDestinatarioid(), denuncia, remitente);

        Mensajes mensaje = new Mensajes();
        mensaje.setId(UUID.randomUUID().toString());
        mensaje.setDenunciaid(denunciaid);
        mensaje.setRemitenteid(remitente.getId());
        mensaje.setDestinatarioid(destinatarioId);
        mensaje.setContenido(request.getContenido());
        mensaje.setLeido(false);
        mensaje.setFechaenvio(Instant.now());

        Mensajes guardado = mensajesRepository.guardar(mensaje);

        String nombreCompleto = remitente.getNombre() + " " + remitente.getApellido();
        return MensajeResponse.from(guardado, nombreCompleto, remitente.getRoles());
    }

    /**
     * Lista todos los mensajes de una denuncia.
     */
    public List<MensajeResponse> listarMensajes(String denunciaid, String usuarioEmail) {
        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada"));

        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(usuarioEmail);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        verificarAccesoDenuncia(denuncia, usuario);

        return mensajesRepository.buscarPorDenuncia(denunciaid).stream()
                .map(m -> {
                    Usuarios remitente = usuariosRepository.buscarPorId(m.getRemitenteid()).orElse(null);
                    String nombre = remitente != null
                            ? remitente.getNombre() + " " + remitente.getApellido()
                            : "Usuario desconocido";
                    String rol = remitente != null ? remitente.getRoles() : "UNKNOWN";
                    return MensajeResponse.from(m, nombre, rol);
                })
                .collect(Collectors.toList());
    }

    /**
     * Marca como leídos todos los mensajes dirigidos al usuario en una denuncia.
     */
    public void marcarComoLeidos(String denunciaid, String usuarioEmail) {
        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(usuarioEmail);
        if (usuario == null) return;

        mensajesRepository
                .buscarNoLeidosPorDenunciaYDestinatario(denunciaid, usuario.getId())
                .forEach(m -> {
                    m.setLeido(true);
                    mensajesRepository.guardar(m);
                });
    }

    /**
     * Cuenta mensajes no leídos del usuario en todas sus denuncias.
     */
    public int contarNoLeidos(String usuarioEmail) {
        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(usuarioEmail);
        if (usuario == null) return 0;
        return mensajesRepository.buscarTodosNoLeidosPorDestinatario(usuario.getId()).size();
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /**
     * Si destinatarioid viene vacío, elige automáticamente:
     * - Víctima → envía al psicólogo (si existe) o al defensor
     * - Psicólogo o Defensor → envía a la víctima (usuarioid)
     * - Admin → envía a la víctima
     */
    private String resolverDestinatario(String destinatarioid, Denuncias denuncia, Usuarios remitente) {
        if (destinatarioid != null && !destinatarioid.isBlank()) {
            return destinatarioid;
        }

        String rol = remitente.getRoles() != null ? remitente.getRoles().toUpperCase() : "";

        switch (rol) {
            case "VICTIM":
                // Preferir psicólogo, si no hay usar defensor
                if (denuncia.getPsicologoId() != null && !denuncia.getPsicologoId().isBlank()) {
                    return denuncia.getPsicologoId();
                }
                if (denuncia.getDefensorLegalId() != null && !denuncia.getDefensorLegalId().isBlank()) {
                    return denuncia.getDefensorLegalId();
                }
                // Sin profesional asignado aún — enviar al creador de la denuncia como fallback
                return denuncia.getUsuarioid();

            case "PSYCHOLOGIST":
            case "DEFENDER":
            case "ADMIN":
            default:
                // Enviar a la víctima
                String victimaId = denuncia.getVictimaId() != null
                        ? denuncia.getVictimaId()
                        : denuncia.getUsuarioid();
                return victimaId;
        }
    }

    private void verificarAccesoDenuncia(Denuncias denuncia, Usuarios usuario) {
        String rol = usuario.getRoles() != null ? usuario.getRoles().toUpperCase() : "";
        boolean esAdmin      = rol.equals("ADMIN");
        boolean esVictima    = usuario.getId().equals(denuncia.getVictimaId())
                || usuario.getId().equals(denuncia.getUsuarioid());
        boolean esPsicologo  = usuario.getId().equals(denuncia.getPsicologoId());
        boolean esDefensor   = usuario.getId().equals(denuncia.getDefensorLegalId());

        if (!esAdmin && !esVictima && !esPsicologo && !esDefensor) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No tienes acceso a esta conversación");
        }
    }
}