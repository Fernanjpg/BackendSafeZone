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
        // 1. Validar ubicación (RF-03): requiere GPS completo O dirección manual
        if (!request.isUbicacionValida()) {
            throw new IllegalArgumentException(
                    "Ubicación inválida: debe proporcionar coordenadas GPS completas (latitud, longitud, precisión) " +
                    "o una dirección manual."
            );
        }

        if (request.getRegion() == null || request.getRegion().getId() == null || request.getRegion().getId().isBlank()) {
            throw new IllegalArgumentException("La región es obligatoria.");
        }

        Regiones regionReal = regionesRepository.buscarPorId(request.getRegion().getId())
                .orElseThrow(() -> new IllegalArgumentException("Región no encontrada"));

        Denuncias denuncia = new Denuncias();

        denuncia.setId(UUID.randomUUID().toString()); // Generamos ID único
        denuncia.setUsuarioid(request.getUsuarioid()); // Importante: Partition Key
        denuncia.setVictimaId(request.getVictimaId());
        denuncia.setTipoViolencia(request.getTipoViolencia());
        denuncia.setDescripcion(request.getDescripcion());
        denuncia.setNivelRiesgo(request.getNivelRiesgo());
        denuncia.setDireccion(request.getDireccion());
        denuncia.setEsAnonima(request.getEsAnonima());

        // 2. Mapear datos de ubicación GPS (RF-03)
        denuncia.setLatitud(request.getLatitud());
        denuncia.setLongitud(request.getLongitud());
        denuncia.setPrecision(request.getPrecision());
        denuncia.setDireccionManual(request.getDireccionManual());
        
        // 3. Determinar fuente de ubicación
        String fuenteUbicacion = determinarFuenteUbicacion(
                request.getLatitud(), 
                request.getLongitud(), 
                request.getDireccionManual()
        );
        denuncia.setFuenteUbicacion(fuenteUbicacion);

        // 4. Seteo de valores por defecto
        denuncia.setEstado("PENDIENTE");
        denuncia.setFechaDenuncia(Instant.now());

        // 5. Creación del objeto embebido (Idéntico a como lo hiciste en Usuarios)
        RegionResumen resumen = new RegionResumen();
        resumen.setId(regionReal.getId());
        resumen.setNombre(regionReal.getNombreRegion());
        denuncia.setRegion(resumen);

        // 6. Guardar en Cosmos DB
        return denunciasRepository.guardar(denuncia);
    }

    /**
     * Determina la fuente de ubicación según los datos disponibles (RF-03).
     * @return "GPS", "MANUAL" o "GPS_Y_MANUAL"
     */
    private String determinarFuenteUbicacion(Double latitud, Double longitud, String direccionManual) {
        boolean tieneGPS = latitud != null && longitud != null;
        boolean tieneDireccionManual = direccionManual != null && !direccionManual.trim().isEmpty();
        
        if (tieneGPS && tieneDireccionManual) {
            return "GPS_Y_MANUAL";
        } else if (tieneGPS) {
            return "GPS";
        } else if (tieneDireccionManual) {
            return "MANUAL";
        }
        return "DESCONOCIDA";
    }

    public void eliminar(Denuncias denuncias) {
        denunciasRepository.eliminar(denuncias);
    }







}
