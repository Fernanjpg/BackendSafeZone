package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Evidencias;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface EvidenciasCrudRepository extends CosmosRepository<Evidencias,String> {
}
