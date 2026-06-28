package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendaCrudRepository extends CosmosRepository<Agenda,String> {
  List<Agenda> findByUsuarioidAndFechaInicioBetween(String usuarioid, LocalDateTime start, LocalDateTime end);
}
