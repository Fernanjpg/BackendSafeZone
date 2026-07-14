package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DenunciasCrudRepository extends CosmosRepository<Denuncias, String> {

    @Query("SELECT * FROM c WHERE c.usuarioid = @usuarioid")
    List<Denuncias> findByUsuarioid(@Param("usuarioid") String usuarioid);

    // Cubre ambos casos: datos nuevos (serializados como "psicologoid" vía @JsonProperty)
    // y datos antiguos asignados antes de agregar la anotación (guardados como "psicologoId").
    @Query("SELECT * FROM c WHERE c.psicologoId = @psicologoid OR c.psicologoid = @psicologoid")
    List<Denuncias> findByPsicologoId(@Param("psicologoid") String psicologoId);

    @Query("SELECT * FROM c WHERE c.defensorLegalId = @defensorlegalid OR c.defensorlegalid = @defensorlegalid")
    List<Denuncias> findByDefensorLegalId(@Param("defensorlegalid") String defensorLegalId);

    @Override
    List<Denuncias> findAll();
}