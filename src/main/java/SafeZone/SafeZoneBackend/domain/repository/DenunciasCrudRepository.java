package SafeZone.SafeZoneBackend.domain.repository;

import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface DenunciasCrudRepository extends CosmosRepository<Denuncias,String> {
}
