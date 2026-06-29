package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.domain.dto.AsignacionCasoRequest;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.ViolenciaRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<Denuncias> buscarDenunciaPorId(String id) {
        return denunciasRepository.buscarPorId(id);
    }
    public Denuncias crearDenuncia(DenunciaRequest request) {
        Denuncias denuncia = new Denuncias();
        denuncia.setId(UUID.randomUUID().toString());

        // Usamos el UUID que viene directamente desde el Frontend
        denuncia.setUsuarioid(request.getUsuarioid());
        denuncia.setVictimaId(request.getUsuarioid());

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


}
