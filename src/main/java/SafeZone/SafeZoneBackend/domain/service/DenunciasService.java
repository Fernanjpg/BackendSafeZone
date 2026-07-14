package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.domain.dto.AsignacionCasoRequest;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.ViolenciaRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class    DenunciasService {

  @Autowired
  public DenunciasRepository denunciasRepository;
  @Autowired
  private UsuariosRepository usuariosRepository;
    @Autowired
    private SeguimientosService seguimientosService;

    @Autowired
    private DenunciaAccessValidator denunciaAccessValidator;
    public List<Denuncias> listarTodas() {
        return denunciasRepository.listar();
    }

    public List<Denuncias> buscarPorVictimaId(String victimId) {
        return denunciasRepository.buscarporusuarioId(victimId);
    }

    public List<Denuncias> listarCasosAsignados(String profesionalId, String rol) {
        String rolNormalizado = rol != null ? rol.toUpperCase() : "";

        // AGREGA ESTO PARA DEPURAR
        System.out.println(">>> Buscando en BD con ID: " + profesionalId);

        List<Denuncias> resultados;
        if (rolNormalizado.contains("PSYCHOLOGIST")) {
            resultados = denunciasRepository.buscarPorPsicologoId(profesionalId);
        } else if (rolNormalizado.contains("DEFENDER")) {
            resultados = denunciasRepository.buscarPorDefensorLegalId(profesionalId);
        } else {
            throw new RuntimeException("Rol no autorizado: " + rolNormalizado);
        }

        // AGREGA ESTO
        System.out.println(">>> Resultados encontrados: " + (resultados != null ? resultados.size() : "NULL"));

        return resultados;
    }
    public Optional<Denuncias> buscarPorUsuarioId(String id) {
        return denunciasRepository.buscarPorId(id);
    }

    // RF-05 — Control de acceso a denuncias por asignación/pertenencia.
    // Valida que el usuario autenticado (extraído del JWT, nunca de un parámetro
    // del cliente) pueda acceder a la denuncia indicada.
    public Denuncias obtenerPorIdConAcceso(String id, String usuarioId) {
        denunciaAccessValidator.validarAcceso(id, usuarioId);
        return denunciasRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada: " + id));
    }
    public Denuncias crearDenuncia(DenunciaRequest request, String usuarioId) {
        Denuncias denuncia = new Denuncias();
        denuncia.setId(UUID.randomUUID().toString());

        // usuarioId viene del JWT (SecurityContext), no del body del request
        denuncia.setUsuarioid(usuarioId);
        denuncia.setVictimaId(usuarioId);

        denuncia.setTitulo(request.getTitulo());
        denuncia.setDescripcion(request.getDescripcion());
        denuncia.setTipoViolencia(request.getTipoViolencia());
        denuncia.setNivelRiesgo(request.getNivelRiesgo());
        denuncia.setEstado(request.getEstado() != null ? request.getEstado() : "PENDIENTE");
        denuncia.setDireccion(request.getDireccion());
        denuncia.setFechaDenuncia(Instant.now());

        Denuncias guardada = denunciasRepository.guardar(denuncia);

        // Registrar hito inicial en el seguimiento
        seguimientosService.registrarHito(
                guardada.getId(),
                "SISTEMA",
                "NOTE",
                "La denuncia ha sido registrada en el sistema con estado: " + guardada.getEstado(),
                null,
                guardada.getEstado()
        );

        return guardada;
    }

    public Denuncias actualizar(String id, DenunciaRequest request) {
        Denuncias existente = denunciasRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Denuncia no encontrada: " + id));

        existente.setTitulo(request.getTitulo());
        existente.setDescripcion(request.getDescripcion());
        existente.setTipoViolencia(request.getTipoViolencia());
        existente.setNivelRiesgo(request.getNivelRiesgo());
        existente.setEstado(request.getEstado());

        return denunciasRepository.guardar(existente);
    }
    //RF-02 — Registrar información de violencia
    public Denuncias registrarViolencia(String denunciaId, ViolenciaRequest request) {
        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new RuntimeException("Denuncia no encontrada: " + denunciaId));

        // Solo actualizar campos que vengan con valor
        if (request.getTipoViolencia() != null) {
            denuncia.setTipoViolencia(request.getTipoViolencia());
        }
        if (request.getDescripcion() != null) {
            denuncia.setDescripcion(request.getDescripcion());
        }
        if (request.getNivelRiesgo() != null) {
            denuncia.setNivelRiesgo(request.getNivelRiesgo());
        }
        if (request.getDireccion() != null) {
            denuncia.setDireccion(request.getDireccion());
        }

        return denunciasRepository.guardar(denuncia);
    }

       // RF-09 — Asignar casos a especialistas

        public Denuncias asignarCaso(String denunciaId, AsignacionCasoRequest request) {
            Denuncias denuncia = denunciasRepository.buscarPorId(denunciaId)
                    .orElseThrow(() -> new RuntimeException("Denuncia no encontrada: " + denunciaId));

        // Validar que no esté ya asignado
        if ("ASIGNADO".equals(denuncia.getEstado())) {
            throw new RuntimeException("La denuncia ya está asignada");
        }

        // Validación explícita: psicologoId es obligatorio. Esto detecta rápido
        // bugs de integración donde el frontend no envía el campo correcto.
        if (request.getPsicologoId() == null || request.getPsicologoId().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "psicologoId es obligatorio para asignar el caso");
        }

        String estadoAnterior = denuncia.getEstado();

        denuncia.setPsicologoId(request.getPsicologoId());
        denuncia.setDefensorLegalId(request.getDefensorLegalId());
        denuncia.setAsignadoPorId(request.getAsignadoPorId());
        denuncia.setNivelRiesgo(request.getPrioridad());
        denuncia.setEstado("ASIGNADO");
        denuncia.setFechaAsignacion(Instant.now());
        
        Denuncias guardada = denunciasRepository.guardar(denuncia);

        // Registrar hito de asignación en el seguimiento
        String desc = String.format("Caso asignado a profesionales. Psicólogo ID: %s, Defensor ID: %s (Asignado por: %s)",
                request.getPsicologoId(), request.getDefensorLegalId(), request.getAsignadoPorId());
        seguimientosService.registrarHito(
                guardada.getId(),
                request.getAsignadoPorId(),
                "NOTE",
                desc,
                estadoAnterior,
                "ASIGNADO"
        );

        return guardada;
    }

    // ELIMINAR
    public void eliminar(Denuncias denuncias) {
        denunciasRepository.eliminar(denuncias);
    }

    // RF-05 — Eliminar por ID (solo ADMIN). Carga la entidad para respetar
    // la partition key antes de borrar en Cosmos.
    public void eliminarPorId(String id) {
        Denuncias denuncia = denunciasRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denuncia no encontrada: " + id));
        denunciasRepository.eliminar(denuncia);
    }

    // TODO: eliminar este endpoint después de ejecutar el backfill una vez en producción
    public record BackfillResult(
            int totalRevisadas,
            int corregidas,
            int yaCorrectas,
            java.util.List<String> noResueltas
    ) {}

    public BackfillResult backfillUsuarioid() {
        java.util.List<Denuncias> todas = denunciasRepository.listar();
        int totalRevisadas = 0;
        int corregidas = 0;
        int yaCorrectas = 0;
        java.util.List<String> noResueltas = new java.util.ArrayList<>();

        for (Denuncias denuncia : todas) {
            totalRevisadas++;
            String usuarioidActual = denuncia.getUsuarioid();

            // Verificar si el usuarioid actual corresponde a un usuario existente
            java.util.Optional<Usuarios> usuarioExistente = usuariosRepository.buscarPorId(usuarioidActual);

            if (usuarioExistente.isPresent()) {
                yaCorrectas++;
                continue;
            }

            // Intentar resolver usando victimaId como email
            String victimaId = denuncia.getVictimaId();
            Usuarios usuarioResuelto = usuariosRepository.buscarUsuarioPorEmail(victimaId);

            if (usuarioResuelto != null) {
                String usuarioidAnterior = denuncia.getUsuarioid();
                String usuarioidNuevo = usuarioResuelto.getId();

                denuncia.setUsuarioid(usuarioidNuevo);
                denuncia.setVictimaId(usuarioidNuevo); // Igual que el flujo actual de creación

                denunciasRepository.guardar(denuncia);
                corregidas++;

                System.out.printf("DENUNCIA CORREGIDA: id=%s, usuarioidAnterior=%s, usuarioidNuevo=%s%n",
                        denuncia.getId(), usuarioidAnterior, usuarioidNuevo);
            } else {
                noResueltas.add(denuncia.getId());
            }
        }

        return new BackfillResult(totalRevisadas, corregidas, yaCorrectas, noResueltas);
    }
}
