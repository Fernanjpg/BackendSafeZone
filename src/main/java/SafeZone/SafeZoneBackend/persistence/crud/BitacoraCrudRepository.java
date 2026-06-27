package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.BitacoraSeguimiento;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BitacoraCrudRepository extends CosmosRepository<BitacoraSeguimiento, String> {

    @Query("SELECT * FROM c WHERE c.denunciaId = @denunciaId ORDER BY c.fechaRegistro ASC")
    List<BitacoraSeguimiento> findByDenunciaId(@Param("denunciaId") String denunciaId);
}