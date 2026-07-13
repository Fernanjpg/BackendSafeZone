package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Seguimientos;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeguimientosCrudRepository extends CosmosRepository<Seguimientos, String> {
    @Query("SELECT * FROM c WHERE c.denunciaid = @denunciaid")
    List<Seguimientos> findByDenunciaid(@Param("denunciaid") String denunciaid);
}
