package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DenunciasCrudRepository extends CosmosRepository<Denuncias, String> {

    @Query("SELECT * FROM c WHERE c.usuarioid = @usuarioid")
    List<Denuncias> buscarPorUsuarioid(
            @Param("usuarioid") String usuarioid
    );
}