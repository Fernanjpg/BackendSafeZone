package SafeZone.SafeZoneBackend.domain.repository;

import SafeZone.SafeZoneBackend.persistence.entity.Mensajes;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface MensajesCrudRepository extends CosmosRepository<Mensajes,String> {
}
