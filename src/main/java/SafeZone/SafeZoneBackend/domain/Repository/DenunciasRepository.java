package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.DenunciasCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DenunciasRepository {

    @Autowired
    private DenunciasCrudRepository crud;

    public List<Denuncias> listar() {

        List<Denuncias> lista = new ArrayList<>();

        crud.findAll().forEach(lista::add);

        return lista;
    }

    public List<Denuncias> buscarporusuarioId(String usuarioId) {
        return crud.buscarPorUsuarioid(usuarioId);
    }

    public Optional<Denuncias> buscarPorId(String id) {
        return crud.findById(id);
    }

    public Denuncias guardar(Denuncias denuncias) {
        return crud.save(denuncias);
    }

    public void eliminar(Denuncias denuncias) {
        crud.delete(denuncias);
    }
}