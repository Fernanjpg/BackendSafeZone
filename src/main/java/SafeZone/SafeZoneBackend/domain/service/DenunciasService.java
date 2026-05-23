package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.DenunciasRepository;
import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.domain.dto.DenunciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.ViolenciaRequest;
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

    // LISTAR TODAS LAS DENUNCIAS
    public List<Denuncias> listarTodas() {
        return denunciasRepository.listar();
    }

    // BUSCAR POR USUARIO ID
    public List<Denuncias> buscarPorUsuarioId(String id) {
        return denunciasRepository.buscarporusuarioId(id);
    }

    // RF-01 CREAR DENUNCIA
    public Denuncias guardar(DenunciaRequest request) {

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

        // valores por defecto
        denuncia.setEstado("PENDIENTE");
        denuncia.setFechaDenuncia(Instant.now());

        // objeto embebido region
        RegionResumen resumen = new RegionResumen();
        resumen.setId(regionReal.getId());
        resumen.setNombre(regionReal.getNombreRegion());
        denuncia.setRegion(resumen);

        // guardar en cosmos
        return denunciasRepository.guardar(denuncia);
    }

    // RF-02 REGISTRAR INFORMACIÓN DE VIOLENCIA
    public Denuncias registrarViolencia(String denunciaId, ViolenciaRequest request) {

        Denuncias denuncia = denunciasRepository.buscarPorId(denunciaId)
                .orElseThrow(() -> new RuntimeException("Denuncia no encontrada"));

        denuncia.setTipoViolencia(request.getTipoViolencia());
        denuncia.setDescripcion(request.getDescripcion());
        denuncia.setNivelRiesgo(request.getNivelRiesgo());
        denuncia.setDireccion(request.getDireccion());

        return denunciasRepository.guardar(denuncia);
    }

    // ELIMINAR
    public void eliminar(Denuncias denuncias) {
        denunciasRepository.eliminar(denuncias);
    }
}