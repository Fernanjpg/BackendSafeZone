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

    public Usuarios buscarUsuarioPorEmail(String email) {
        List<Usuarios> resultados = crud.findByEmail(email);
        // Retorna el primero si existe, sino null
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public Usuarios buscarUsuarioPorEmailYRol(String email, String roles) {
        List<Usuarios> resultados = crud.findByEmailAndRoles(email, roles);
        return resultados.isEmpty() ? null : resultados.get(0);
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
