package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.AlertaEmergenciaCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.AlertaEmergencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlertaEmergenciaRepository {

    @Autowired
    private AlertaEmergenciaCrudRepository crud;

    public List<AlertaEmergencia> listarTodas() {
        return crud.findAll();
    }

    public List<AlertaEmergencia> buscarPorEstado(String estado) {
        return crud.findByEstado(estado);
    }

    public List<AlertaEmergencia> buscarPorVictima(String victimaId) {
        return crud.findByVictimaId(victimaId);
    }

    public Optional<AlertaEmergencia> buscarPorId(String id) {
        return crud.findById(id);
    }

    public AlertaEmergencia guardar(AlertaEmergencia alerta) {
        return crud.save(alerta);
    }
}
