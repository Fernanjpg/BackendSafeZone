package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface RegionesCrudRepository extends CosmosRepository<Regiones,String> {

}
