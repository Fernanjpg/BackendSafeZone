package SafeZone.SafeZoneBackend.domain.Repository;

import SafeZone.SafeZoneBackend.persistence.crud.DenunciasCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Denuncias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class DenunciasRepository {
    @Autowired
    private DenunciasCrudRepository crud;

    public List<Denuncias> listar (){
        return crud.findAll();
    }

    public List<Denuncias> buscarporusuarioId(String usuarioId) {
        return crud.findByUsuarioId(usuarioId);
    }
    public Denuncias guardar(Denuncias denuncias) {
        return crud.save(denuncias);
    }
    public void eliminar(Denuncias denuncias){
        crud.deleteAll();
    }

}
