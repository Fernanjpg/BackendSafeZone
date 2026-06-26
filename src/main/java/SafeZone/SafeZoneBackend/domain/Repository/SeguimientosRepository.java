package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.SeguimientosCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Seguimientos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeguimientosRepository {
    @Autowired
    private SeguimientosCrudRepository crud;

    public List<Seguimientos> buscarPorDenunciaId(String denunciaId) {
        return crud.findByDenunciaid(denunciaId);
    }

    public Optional<Seguimientos> buscarPorId(String id) {
        return crud.findById(id);
    }

    public Seguimientos guardar(Seguimientos seguimiento) {
        return crud.save(seguimiento);
    }

    public void eliminar(Seguimientos seguimiento) {
        crud.delete(seguimiento);
    }
}
