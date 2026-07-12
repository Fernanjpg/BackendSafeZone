package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.AgendaCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AgendaRepository {

    private final AgendaCrudRepository agendaCrudRepository;
    @Autowired
    public AgendaRepository(AgendaCrudRepository agendaCrudRepository) {
        this.agendaCrudRepository = agendaCrudRepository;
    }

    public List<Agenda> AgendarCitas(String usuarioid, String start, String  end) {
        return agendaCrudRepository.buscarPorUsuarioYRangoDeFechas(usuarioid, start, end);
    }

    public Optional<Agenda> findById(String id) {
        return agendaCrudRepository.findById(id);
    }
    public List<Agenda> buscarCruces(String usuarioid, String inicio, String fin) {
        return agendaCrudRepository.buscarPorUsuarioYRangoDeFechas(usuarioid, inicio, fin);
    }

    public List<Agenda> listarPorVictimaDirecto(String usuarioid) {
        return agendaCrudRepository.findAllByUsuarioid(usuarioid);
    }

    public List<Agenda> listarPorPsicologoDirecto(String profesionalId) {
        return agendaCrudRepository.findAllByProfesionalId(profesionalId);
    }

    public Agenda guardar(Agenda agenda) {
        return agendaCrudRepository.save(agenda);
    }
}
