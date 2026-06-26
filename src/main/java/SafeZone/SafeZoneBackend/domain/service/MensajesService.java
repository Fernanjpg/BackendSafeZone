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
     * Envía un mensaje dentro de una denuncia (conversación).
     * El remitente es el usuario autenticado (email del JWT).
     */
    public MensajeResponse enviarMensaje(String denunciaid, MensajeRequest request, String remitenteEmail) {
        // Verificar que la denuncia existe
        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada: " + denunciaid));

        // Obtener datos del remitente
        Usuarios remitente = usuariosRepository.buscarUsuarioPorEmail(remitenteEmail);
        if (remitente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Remitente no encontrado");
        }

        // Verificar que el destinatario existe
        Usuarios destinatario = usuariosRepository.buscarPorId(request.getDestinatarioid())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Destinatario no encontrado"));

        // Construir y guardar el mensaje
        Mensajes mensaje = new Mensajes();
        mensaje.setId(UUID.randomUUID().toString());
        mensaje.setDenunciaid(denunciaid);
        mensaje.setRemitenteid(remitente.getId());
        mensaje.setDestinatarioid(destinatario.getId());
        mensaje.setContenido(request.getContenido());
        mensaje.setLeido(false);
        mensaje.setFechaenvio(Instant.now());

        Mensajes guardado = mensajesRepository.guardar(mensaje);

        String nombreCompleto = remitente.getNombre() + " " + remitente.getApellido();
        return MensajeResponse.from(guardado, nombreCompleto, remitente.getRoles());
    }

    /**
     * Lista todos los mensajes de una denuncia, enriquecidos con nombre y rol del remitente.
     */
    public List<MensajeResponse> listarMensajes(String denunciaid, String usuarioEmail) {
        // Verificar acceso: el usuario debe ser parte de la denuncia
        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaid)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada"));

        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(usuarioEmail);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        verificarAccesoDenuncia(denuncia, usuario);

        List<Mensajes> mensajes = mensajesRepository.buscarPorDenuncia(denunciaid);

        return mensajes.stream()
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
     * Marca como leídos todos los mensajes dirigidos al usuario autenticado en una denuncia.
     */
    public void marcarComoLeidos(String denunciaid, String usuarioEmail) {
        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(usuarioEmail);
        if (usuario == null) return;

        List<Mensajes> noLeidos = mensajesRepository
                .buscarNoLeidosPorDenunciaYDestinatario(denunciaid, usuario.getId());

        noLeidos.forEach(m -> {
            m.setLeido(true);
            mensajesRepository.guardar(m);
        });
    }

    /**
     * Cuenta mensajes no leídos del usuario autenticado (en todas sus denuncias).
     */
    public int contarNoLeidos(String usuarioEmail) {
        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(usuarioEmail);
        if (usuario == null) return 0;
        return mensajesRepository.buscarTodosNoLeidosPorDestinatario(usuario.getId()).size();
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    private void verificarAccesoDenuncia(Denuncias denuncia, Usuarios usuario) {
        String rol = usuario.getRoles() != null ? usuario.getRoles().toUpperCase() : "";
        boolean esAdmin = rol.equals("ADMIN");
        boolean esVictima =
                usuario.getId().equals(denuncia.getVictimaId())
                        || usuario.getId().equals(denuncia.getUsuarioid())
                        || usuario.getEmail().equals(denuncia.getVictimaId());
        boolean esPsicologo = usuario.getId().equals(denuncia.getPsicologoId());
        boolean esDefensor = usuario.getId().equals(denuncia.getDefensorLegalId());

        if (!esAdmin && !esVictima && !esPsicologo && !esDefensor) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "No tienes acceso a esta conversación");
        }
    }
}