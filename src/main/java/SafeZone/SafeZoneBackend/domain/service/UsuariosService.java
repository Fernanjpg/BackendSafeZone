package SafeZone.SafeZoneBackend.domain.service;



import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service

public class UsuariosService {
@Autowired
private UsuariosRepository usuariosRepository;

@Autowired
private RegionesRepository regionesRepository;

public List<Usuarios> listar() {
    return usuariosRepository.listar();
}
    public Usuarios validarLogin(String email, String password) {
        Usuarios user = usuariosRepository.buscarUsuarioPorEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public Usuarios registrar(RegisterRequest request) {
        Usuarios existente = usuariosRepository.buscarUsuarioPorEmail(request.getEmail())
                ;
        if (existente != null) {
            throw new RuntimeException("Correo ya registrado");
        }
        Regiones regionReal = regionesRepository.buscarPorId(request.getRegion().getId())
                .orElseThrow(() -> new RuntimeException("Región no encontrada"));
        Usuarios user = new Usuarios();

        user.setId(UUID.randomUUID().toString());
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setTelefono(request.getTelefono());
        user.setRoles(request.getRoles());

        RegionResumen resumen = new RegionResumen();
        resumen.setId(regionReal.getId());
        resumen.setNombre(regionReal.getNombreRegion());
        user.setRegion(resumen);

        user.setEstado("ACTIVO");
        user.setFecharegistro(java.time.Instant.now());

        return usuariosRepository.guardar(user);
    }
  public void eliminarusuario(Usuarios usuarios) {
      usuariosRepository.eliminar(usuarios);
  }






}






