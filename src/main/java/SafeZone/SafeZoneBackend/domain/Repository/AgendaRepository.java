package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.AgendaCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class AgendaRepository {

    private final AgendaCrudRepository agendaCrudRepository;

    public AgendaRepository(AgendaCrudRepository agendaCrudRepository) {
        this.agendaCrudRepository = agendaCrudRepository;
    }

    public List<Agenda> AgendarCitas(String usuarioid, LocalDateTime start, LocalDateTime  end) {
        return agendaCrudRepository.findByUsuarioidAndFechaInicioBetween(usuarioid, start, end);
    }

    public Agenda guardar(Agenda agenda) {
        return agendaCrudRepository.save(agenda);
    }
    public Optional<Agenda> BuscarporId (String id) {
        return agendaCrudRepository.findById(id);
    }
}
