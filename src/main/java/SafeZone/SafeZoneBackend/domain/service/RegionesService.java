package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RegionesService {
    @Autowired
    private RegionesRepository regionesRepository;

    public List<Regiones> listarTodas() {
        return (List<Regiones>) regionesRepository.listarTodas();
    }

    public Regiones crear(Regiones region) {
        return regionesRepository.guardar(region);
    }

    public Regiones buscarPorId(String id) {
        return regionesRepository.buscarPorId(id).orElse(null);
    }
}
