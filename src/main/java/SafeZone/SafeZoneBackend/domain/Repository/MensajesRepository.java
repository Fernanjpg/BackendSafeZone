package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.MensajesCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Mensajes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MensajesRepository {

    @Autowired
    private MensajesCrudRepository crud;

    public Mensajes guardar(Mensajes mensaje) {
        return crud.save(mensaje);
    }

    public Optional<Mensajes> buscarPorId(String id) {
        return crud.findById(id);
    }

    public List<Mensajes> buscarPorDenuncia(String denunciaid) {
        return crud.findByDenunciaid(denunciaid);
    }

    public List<Mensajes> buscarNoLeidosPorDenunciaYDestinatario(String denunciaid, String destinatarioid) {
        return crud.findUnreadByDenunciaAndDestinatario(denunciaid, destinatarioid);
    }

    public List<Mensajes> buscarTodosNoLeidosPorDestinatario(String destinatarioid) {
        return crud.findAllUnreadByDestinatario(destinatarioid);
    }

    public void eliminar(String id) {
        crud.deleteById(id);
    }
}