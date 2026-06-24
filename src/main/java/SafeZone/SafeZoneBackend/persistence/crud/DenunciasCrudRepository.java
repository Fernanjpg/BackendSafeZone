package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DenunciasCrudRepository extends CosmosRepository<Denuncias, String> {

    @Query("SELECT * FROM c WHERE c.usuarioid = @usuarioid")
    List<Denuncias> findByUsuarioid(@Param("usuarioid") String usuarioid);

    // Cambiamos el nombre del campo en la consulta SQL para que coincida con el JSON
    @Query("SELECT * FROM c WHERE c.psicologoid = @psicologoid")
    List<Denuncias> findByPsicologoId(@Param("psicologoid") String psicologoId);

    @Query("SELECT * FROM c WHERE c.defensorlegalid = @defensorlegalid")
    List<Denuncias> findByDefensorLegalId(@Param("defensorlegalid") String defensorLegalId);

    @Override
    List<Denuncias> findAll();
}