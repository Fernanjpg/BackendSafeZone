package SafeZone.SafeZoneBackend.domain.repository;

import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

public interface UsuariosCrudRepository extends CosmosRepository<Usuarios,String> {
}
