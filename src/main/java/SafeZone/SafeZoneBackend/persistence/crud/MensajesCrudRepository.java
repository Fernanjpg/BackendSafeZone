package SafeZone.SafeZoneBackend.persistence.crud;

import SafeZone.SafeZoneBackend.persistence.entity.Mensajes;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;

import java.util.List;

public interface MensajesCrudRepository extends CosmosRepository<Mensajes, String> {

    // Todos los mensajes de una denuncia (partition key)
    List<Mensajes> findByDenunciaid(String denunciaid);

    // Mensajes no leídos de un destinatario en una denuncia
    @Query("SELECT * FROM c WHERE c.denunciaid = @denunciaid AND c.destinatarioid = @destinatarioid AND c.leido = false")
    List<Mensajes> findUnreadByDenunciaAndDestinatario(String denunciaid, String destinatarioid);

    // Contar no leídos de un usuario (todas sus denuncias)
    @Query("SELECT * FROM c WHERE c.destinatarioid = @destinatarioid AND c.leido = false")
    List<Mensajes> findAllUnreadByDestinatario(String destinatarioid);
}