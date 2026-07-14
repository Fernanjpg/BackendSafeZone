package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Evidencia;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvidenciaCrudRepository extends CosmosRepository<Evidencia, String> {

    @Query("SELECT * FROM c WHERE c.denunciaid = @denunciaid")
    List<Evidencia> findByDenunciaid(@Param("denunciaid") String denunciaid);

    @Override
    List<Evidencia> findAll();
}