package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgendaCrudRepository extends CosmosRepository<Agenda,String> {

  @Query("SELECT * FROM c WHERE c.usuarioid = @usuarioid AND c.fechaInicio >= @start AND c.fechaFin <= @end")
  List<Agenda> buscarPorUsuarioYRangoDeFechas(
          @Param("usuarioid") String usuarioid,
          @Param("start") String start,
          @Param("end") String end
  );

  @Query("SELECT * FROM c WHERE c.usuarioid = @usuarioid")
  List<Agenda> findAllByUsuarioid(@Param("usuarioid") String usuarioid);

  @Query("SELECT * FROM c WHERE c.profesionalId = @profesionalId")
  List<Agenda> findAllByProfesionalId(@Param("profesionalId") String profesionalId);
}