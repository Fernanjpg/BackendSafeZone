package SafeZone.SafeZoneBackend.domain.Repository;


import SafeZone.SafeZoneBackend.persistence.crud.UsuariosCrudRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UsuariosRepository {
    @Autowired
    private UsuariosCrudRepository crud;

    public Usuarios buscarUsuarioPorEmail(String email){
        return crud.findByEmail(email).orElse(null);
    }
   public Usuarios guardar(Usuarios usuarios){
        return crud.save(usuarios);
   }
   public List<Usuarios> listar() {
        return crud.findAll();
   }
   public void eliminar(Usuarios usuarios) {
         crud.delete(usuarios);
   }

}
