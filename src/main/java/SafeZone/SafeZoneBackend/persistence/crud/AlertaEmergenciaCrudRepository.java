package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.AlertaEmergencia;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

import java.util.List;

public interface AlertaEmergenciaCrudRepository extends CosmosRepository<AlertaEmergencia, String> {

    // Todas las alertas de una víctima
    List<AlertaEmergencia> findByVictimaId(String victimaId);

    // Para que los profesionales vean solo las activas
    List<AlertaEmergencia> findByEstado(String estado);

    @Override
    List<AlertaEmergencia> findAll();
}
