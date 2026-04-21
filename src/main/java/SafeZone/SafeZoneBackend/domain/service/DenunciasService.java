package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Regiones regionReal = regionesRepository.buscarPorId(request.getRegion().getId())
                .orElseThrow(() -> new RuntimeException("Región no encontrada"));


        Denuncias denuncia = new Denuncias();

        denuncia.setId(UUID.randomUUID().toString()); // Generamos ID único
        denuncia.setUsuarioid(request.getUsuarioid()); // Importante: Partition Key
        denuncia.setVictimaId(request.getVictimaId());
        denuncia.setTipoViolencia(request.getTipoViolencia());
        denuncia.setDescripcion(request.getDescripcion());
        denuncia.setNivelRiesgo(request.getNivelRiesgo());
        denuncia.setDireccion(request.getDireccion());
        denuncia.setEsAnonima(request.getEsAnonima());

        // 3. Seteo de valores por defecto
        denuncia.setEstado("PENDIENTE");
        denuncia.setFechaDenuncia(Instant.now());

        // 4. Creación del objeto embebido (Idéntico a como lo hiciste en Usuarios)
        RegionResumen resumen = new RegionResumen();
        resumen.setId(regionReal.getId());
        resumen.setNombre(regionReal.getNombreRegion());
        denuncia.setRegion(resumen);

        // 5. Guardar en Cosmos DB
        return denunciasRepository.guardar(denuncia);
    }

    public void eliminar(Denuncias denuncias) {
        denunciasRepository.eliminar(denuncias);
    }







}
