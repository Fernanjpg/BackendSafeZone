package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosCrudRepository extends CosmosRepository<Usuarios,String> {

   @Override
   List<Usuarios> findAll();
   @Query(value = "SELECT * FROM c WHERE c.email = @email")
   List<Usuarios> findByEmail(@Param("email") String email);

   @Query(value = "SELECT * FROM c WHERE c.email = @email AND c.roles = @roles")
   List<Usuarios> findByEmailAndRoles(@Param("email") String email, @Param("roles") String roles);
}
