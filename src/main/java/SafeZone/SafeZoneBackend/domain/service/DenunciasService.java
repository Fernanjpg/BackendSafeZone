package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SafeZone.SafeZoneBackend.domain.dto.AsignacionCasoRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DenunciasService {

  @Autowired
  public DenunciasRepository denunciasRepository;
  @Autowired
    private RegionesRepository regionesRepository;

    public List<Denuncias> listarTodas() {
        return denunciasRepository.listar();
    }

    public List<Denuncias> buscarPorUsuarioId(String id) {
        return denunciasRepository.buscarporusuarioId(id);
    }
    public Denuncias guardar(DenunciaRequest request) {

        try {

            System.out.println("🔥 REQUEST RECIBIDO");
            System.out.println(request.getUsuarioid());
            System.out.println(request.getVictimaId());
            System.out.println(request.getTipoViolencia());
            System.out.println(request.getDescripcion());

            Regiones regionReal = regionesRepository.buscarPorId(request.getRegion().getId())
                    .orElseThrow(() -> new RuntimeException("Región no encontrada"));

            Denuncias denuncia = new Denuncias();

            denuncia.setId(UUID.randomUUID().toString());
            denuncia.setUsuarioid(request.getUsuarioid());
            denuncia.setVictimaId(request.getVictimaId());
            denuncia.setTipoViolencia(request.getTipoViolencia());
            denuncia.setDescripcion(request.getDescripcion());
            denuncia.setNivelRiesgo(request.getNivelRiesgo());
            denuncia.setDireccion(request.getDireccion());
            denuncia.setEsAnonima(request.getEsAnonima());

            denuncia.setEstado("PENDIENTE");
            denuncia.setFechaDenuncia(Instant.now());

            RegionResumen resumen = new RegionResumen();
            resumen.setId(regionReal.getId());
            resumen.setNombre(regionReal.getNombreRegion());

            denuncia.setRegion(resumen);

            System.out.println("🔥 GUARDANDO EN COSMOS");

            try {

                Denuncias guardada = denunciasRepository.guardar(denuncia);

                System.out.println("✅ GUARDADO EXITOSO");

                return guardada;

            } catch (Exception e) {

                System.out.println("💥 ERROR COSMOS:");
                e.printStackTrace();

                throw e;
            }

        } catch (Exception e) {

            System.out.println("💥 ERROR REAL:");
            e.printStackTrace();

            throw e;
        }
    }

    public Denuncias asignarCaso(String denunciaId, AsignacionCasoRequest request) {

        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new RuntimeException("Denuncia no encontrada"));

        denuncia.setPsicologoId(request.getPsicologoId());
        denuncia.setDefensorLegalId(request.getDefensorLegalId());
        denuncia.setAsignadoPorId(request.getAsignadoPorId());
        denuncia.setNivelRiesgo(request.getPrioridad());

        denuncia.setEstado("ASIGNADO");
        denuncia.setFechaAsignacion(Instant.now());

        return denunciasRepository.guardar(denuncia);
    }

    public void eliminar(Denuncias denuncias) {
        denunciasRepository.eliminar(denuncias);
    }







}
