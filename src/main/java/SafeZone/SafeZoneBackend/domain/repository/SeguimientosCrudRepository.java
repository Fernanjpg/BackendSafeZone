package SafeZone.SafeZoneBackend.domain.repository;

import SafeZone.SafeZoneBackend.persistence.entity.Seguimientos;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface SeguimientosCrudRepository extends CosmosRepository<Seguimientos,String> {
}
