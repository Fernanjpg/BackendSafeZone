package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import com.azure.spring.data.cosmos.repository.CosmosRepository;

import java.util.List;
import java.util.Optional;

public interface UsuariosCrudRepository extends CosmosRepository<Usuarios,String> {

   @Override
   List<Usuarios> findAll();
   Optional<Usuarios> findByEmail(String email);
}
